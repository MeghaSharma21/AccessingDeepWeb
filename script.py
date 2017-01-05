from bs4 import BeautifulSoup
import requests
import urllib
import re
import json
import unicodedata

path = 'C:\Users\Megha Sharma\Documents\NetBeansProjects\JavaApplication2\goodForms'

field = ""
with open(path) as f:
    lines = f.read().splitlines()
synonyms = ["By", "by", "Author", "author", "person", "Person"]
#names of authors
interestingConcepts = ["Megha", "Saket" , "Kiran" , "Manish"]

for i in lines :
    entry = i.split("\t")
    inputUrl = entry[0]
    fid = entry[1]
    #inputUrl = 'https://searchworks.stanford.edu/'
    role = "search"
    fp = urllib.urlopen(inputUrl)
    soup = BeautifulSoup(fp)
    if fid is "":
       inputTag = soup.find("form", {"role": role})
    else:
        inputTag = soup.find("form", {"id": fid})

    action = inputTag.get("action")
    if "http" in action :
        baseUrl = ""
    else :
        if(".com" in inputUrl) :
            index = inputUrl.index(".com")
        else:
            if (".edu" in inputUrl):
                index = inputUrl.index(".edu")
            else:
                if (".in" in inputUrl):
                    index = inputUrl.index(".in")
        baseUrl = inputUrl[0 : index + 4]


    fields = inputTag.findAll("input",{"type" : "text"})
    method = inputTag.get("method")
    if method is None:
      method = u'get'
    for oName in synonyms:
        if (len(fields) > 1):
            fields = inputTag.get("input", {"name": oName})
            if fields is None :
                continue
            for author in interestingConcepts:
                payload = {oName: author}
                if 'post' in method:
                    r = requests.post(baseUrl + action, params=payload)
                else:
                    r = requests.get(baseUrl + action, params=payload)
                    soup1 = BeautifulSoup(r.text)
                    hrefs = soup1.find_all('a', href=True)
                    regp = ".*page=\d+.*"
                    regs = '.*start=\d+.*'
                    regp = re.compile(regp)
                    regs = re.compile(regs)
                    pageshain = False
                    startshain = False
                    for a in hrefs:
                        if bool(regp.search(str(a))):
                            pageshain = True
                        if bool(regs.search(r.text)):
                            startshain = True

                    if pageshain:
                        print 'Enters in to PAGE'
                        pagenumber = 0
                        for i in xrange(0, 6):
                            payload['page'] = pagenumber = pagenumber + 1
                            r = requests.get(baseUrl + action, params=payload)
                            body = r.text
                            soup1 = BeautifulSoup(body)
                            towrite = str(soup1.body)
                            sitename = str(baseUrl) + str(action)
                            sitename = sitename.replace("/","")
                            filename = str(pagenumber) +str(payload[oName])+ sitename 
                            fo = open(filename + ".txt", "w")
                            fo.write(towrite)
                            fo.close()
                            print "\nCompleted " + filename + ".txt"
                    else:
                        if startshain:
                            print 'Enters in to START'
                            start = -9
                            for i in xrange(0, 6):
                                payload['start'] = start = start + 10
                                r = requests.get(baseUrl + action, params=payload)
                                body = r.text
                                soup1 = BeautifulSoup(body)
                                towrite = str(soup1.body)
                                sitename = str(baseUrl) + str(action)
                                sitename = sitename.replace("/","")
                                filename = str(start) +str(payload[oName])+ sitename 
                                #print filename
                                fo = open(filename+".txt", "w")
                                fo.write(towrite)
                                fo.close()
                                print "\nCompleted " + filename + ".txt"

    else:
            for x in fields:	
				for name in x:
					if name == "name":
						field = x["name"]
					else:
						field = ""
													
            ######YEHHH BHI SOLVE HO GYI, I GUESS!! I DIDNT TEST IT THOUGH
            if fields=="":
				continue
            for author in interestingConcepts:
                payload = {field: author}
                ###########Method ki type ka ab koi issue nhn
                if 'post' in method :
                    ## Post mein kaunsa url de?
                    r = requests.post(baseUrl + action, params=payload)
                else :
                    r = requests.get(baseUrl + action, params=payload)
