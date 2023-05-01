import json
import requests
import threading
import time

def main():
    fetchSentencesParams = {
        "loremIpsumSentences": [],
        "threads": [],
    }
    
    CONTACT_NUM = 200
    def requestLoremIpsumSentence(dict):
        response = requests.get("https://loripsum.net/api/1/short/plaintext", verify=False)
        dict["loremIpsumSentences"].append(response.text)
    
    start = time.time()
    for _ in range(CONTACT_NUM):
        thread = threading.Thread(target=requestLoremIpsumSentence, args=(fetchSentencesParams,))
        fetchSentencesParams["threads"].append(thread)
        thread.start()
    
    for thread in fetchSentencesParams["threads"]:
        thread.join()

    end = time.time()
    print ("Async: " + str(end - start))



    fetchParaParams = {
        "loremIpsumParas": [],
        "threads": [],
    }
    
    CONTACT_NUM = 100
    def requestLoremIpsumPara(dict):
        response = requests.get("https://loripsum.net/api/1/long/plaintext", verify=False)
        dict["loremIpsumParas"].append(response.text)
    
    for _ in range(CONTACT_NUM):
        thread = threading.Thread(target=requestLoremIpsumPara, args=(fetchParaParams,))
        fetchParaParams["threads"].append(thread)
        thread.start()
    
    for thread in fetchParaParams["threads"]:
        thread.join()



    # myDict = {
    #     "loremIpsumSentences": [],
    #     "threads": [],
    # }
    # CONTACT_NUM = 60
    
    # start = time.time()
    # for i in range(CONTACT_NUM):
    #     response = requests.get("https://baconipsum.com/api/?type=all-meat&sentences=1", verify=False)
    #     myDict["loremIpsumSentences"].append(response.text)

    # end = time.time()
    # print ("Sync: " + str(end - start))
    


    loremIpsum = {
        "sentences": fetchSentencesParams["loremIpsumSentences"],
        "paras": fetchParaParams["loremIpsumParas"]
    }
    outFile = open("testData/loremIpsum.json", "w")
    outFile.write(json.dumps(loremIpsum))

main()
