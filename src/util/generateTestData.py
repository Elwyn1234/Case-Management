import json
import random
import requests
import threading
import time

def main():
    file = open("TestDataTemplate.json", "r")
    text = file.read()
    
    obj = json.loads(text)
    
    funcParams = {
        "loremIpsumArray": [],
        "threads": [],
    }
    
    CONTACT_NUM = 11
    def requestLoremIpsum(dict):
        response = requests.get("https://loripsum.net/api/1/short/plaintext", verify=False)
        dict["loremIpsumArray"].append(response.text)
    
    start = time.time()
    for i in range(CONTACT_NUM):
        thread = threading.Thread(target=requestLoremIpsum, args=(funcParams,))
        funcParams["threads"].append(thread)
        thread.start()
    
    i = 0
    for thread in funcParams["threads"]:
        thread.join()
        i += 1
        # print(i)

    end = time.time()
    print ("Async: " + str(end - start))



    # myDict = {
    #     "loremIpsumArray": [],
    #     "threads": [],
    # }
    # CONTACT_NUM = 60
    
    # start = time.time()
    # for i in range(CONTACT_NUM):
    #     response = requests.get("https://baconipsum.com/api/?type=all-meat&sentences=1", verify=False)
    #     myDict["loremIpsumArray"].append(response.text)

    # end = time.time()
    # print ("Sync: " + str(end - start))
    


    obj["contacts"] = []
    for i in range(CONTACT_NUM):
        day = random.randint(1, 28)
        month = random.randint(1, 12)
        year = 2023
        if (month > 4 and day > 15) or month > 5:
            year = 2022
        contactMethod = "";
        x = random.randint(1, 4)
        if x == 1:
            contactMethod = "Phone"
        if x == 2:
            contactMethod = "Email"
        if x == 3:
            contactMethod = "Post"
        if x == 4:
            contactMethod = "Other"
    
        obj["contacts"].append({
            "description": funcParams["loremIpsumArray"][i],
            "date": {
                "day": day,
                "month": month,
                "year": year,
            },
            "contactMethod": contactMethod,
            "case": 1,
            "user": 1,
            "time": {
                "second": random.randint(0, 59),
                "minute": random.randint(0, 59),
                "hour": random.randint(0, 23),
            }
        })
    
    outFile = open("testData.json", "w")
    outFile.write(json.dumps(obj))
    
    # print(json.dumps(obj))

main()
