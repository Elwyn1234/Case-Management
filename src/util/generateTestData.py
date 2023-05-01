import json
import random
import math;

def main():
    file = open("testData/testDataTemplate.json", "r")
    text = file.read()
    obj = json.loads(text)

    file = open("testData/loremIpsum.json", "r")
    text = file.read()
    loremIpsum = json.loads(text)
    
    sentences = []
    for i in range(len(loremIpsum["sentences"])):
        sentences.append(loremIpsum["sentences"][i])

    paras = []
    for i in range(len(loremIpsum["paras"])):
        paras.append(loremIpsum["paras"][i])

    obj["contacts"] = []
    for i in range(1100):
        date = generateDate()

        contactMethod = "";
        x = random.randint(1, 100)
        if x < 42:
            contactMethod = "Phone"
        elif x < 78:
            contactMethod = "Email"
        elif x < 86:
            contactMethod = "Post"
        else:
            contactMethod = "Other"
    
        obj["contacts"].append({})
        obj["contacts"][i]["description"] = sentences[random.randrange(0, len(sentences))]
        obj["contacts"][i]["contactMethod"] = contactMethod
        obj["contacts"][i]["case"] = random.randint(1, 100)
        obj["contacts"][i]["user"] = 1
        obj["contacts"][i]["day"] = date["day"]
        obj["contacts"][i]["month"] = date["month"]
        obj["contacts"][i]["year"] = date["year"]
        obj["contacts"][i]["second"] = random.randint(0, 59)
        obj["contacts"][i]["minute"] = random.randint(0, 59)
        obj["contacts"][i]["hour"] = random.randint(0, 23)



    obj["cases"] = []
    for i in range(120):
        date = generateDate()

        priority = ""
        x = random.randint(1, 100)
        if x < 5:
            priority = "Urgent"
        elif x < 20:
            priority = "High"
        elif x < 62:
            priority = "Medium"
        else:
            priority = "Low"

        obj["cases"].append({})
        obj["cases"][i]["summary"] = sentences[random.randrange(0, len(sentences))]
        obj["cases"][i]["description"] = paras[random.randrange(0, len(paras))]
        obj["cases"][i]["customer"] = 2
        obj["cases"][i]["assignedTo"] = random.randint(1, 7)
        obj["cases"][i]["createdBy"] = random.randint(1, 7)
        obj["cases"][i]["priority"] = priority

        obj["cases"][i]["dayOpened"] = date["day"]
        obj["cases"][i]["monthOpened"] = date["month"]
        obj["cases"][i]["yearOpened"] = date["year"]
        obj["cases"][i]["secondOpened"] = random.randint(0, 59)
        obj["cases"][i]["minuteOpened"] = random.randint(0, 59)
        obj["cases"][i]["hourOpened"] = random.randint(0, 23)



    for i in range(len(obj["customers"])):
        date = generateDate()

        gender = ""
        x = random.randint(1, 100)
        if x < 3:
            gender = "Non-Binary"
        elif x < 4:
            gender = "Other"
        elif x < 55:
            gender = "Male"
        else:
            gender = "Female"

        email = obj["customers"][i]["firstName"] + obj["customers"][i]["secondName"] + "@example.com"

        obj["customers"][i]["gender"] = gender
        obj["customers"][i]["otherNotes"] = sentences[random.randrange(0, len(sentences))]
        obj["customers"][i]["dayOfBirth"] = date["day"]
        obj["customers"][i]["monthOfBirth"] = date["month"]
        obj["customers"][i]["yearOfBirth"] = date["year"]
        obj["customers"][i]["email"] = email
        obj["customers"][i]["phoneNumber"] = "07460 712095"



    for i in range(len(obj["subscriptions"])):
        obj["subscriptions"][i]["name"] = "Subscription " + i
        obj["subscriptions"][i]["price"] = i * 500
        obj["subscriptions"][i]["description"] = paras[random.randrange(0, len(paras))]



    # for i in range(len(obj["users"])):
    #     print(type(obj["users"][i]["password"]))
    #     obj["users"][i]["password"] = bcrypt.hashpw(obj["users"][i]["password"].decode("utf8"), bcrypt.gensalt())
    
    outFile = open("testData/testData.json", "w")
    outFile.write(json.dumps(obj))



def generateDate():
    day = random.randint(1, 28)

    x = random.randint(1, 100)
    x = float(x) / 100 * 1.57 # need radian values between 0 and 0.5
    y = math.sin(x)
    month = int(y * 12)
    month = min(month, 11)
    # Hack to get the most busy months to be the most recent (May)
    month = month + 5
    if month > 11:
        month = month - 12

    year = 2023
    if month > 3:
        year = 2022

    return {
        "day": day,
        "month": month,
        "year": year
    }



main()
