package main

import (
	"database/sql"
	"encoding/json"
	"log"
	"os"

	"github.com/fatih/color"
	_ "github.com/mattn/go-sqlite3"
)

func main() {
  var errorWriter ErrorWriter
  logError = log.New(errorWriter, "ERROR: ", log.Ldate|log.Ltime|log.Lshortfile)

  dropDatabase()
  createdb()
  addTestData()
}

func openDatabase() (pool *sql.DB) {
  pool, err := sql.Open("sqlite3", "../../assets/caseManagement.db") // TODO: get the password from a file
  if (err != nil) { logError.Fatal(err.Error()) } // TODO: error handling

  pool.SetConnMaxLifetime(0)
  pool.SetMaxIdleConns(1)
  pool.SetMaxOpenConns(1)
  if err := pool.Ping(); err != nil { logError.Fatal(err.Error()) }

  return pool
}

func dropDatabase() {
  log.Print("Dropping caseManagement Database.")
  err := os.Remove("../../assets/caseManagement.db")
  if (err != nil && !os.IsNotExist(err)) { logError.Fatal(err.Error()) }
  log.Print("Database dropped!")
}

func createdb() {
  pool := openDatabase()

  _, err := pool.Exec(`CREATE TABLE users (
    username VARCHAR(32) NOT NULL,
    password VARCHAR(32) NOT NULL,
    name VARCHAR(32) NOT NULL,
    role VARCHAR(32) NOT NULL,
    PRIMARY KEY (username)
  );`) // TODO: Do we want a primary key here
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("users table created!")

  _, err = pool.Exec(`CREATE TABLE customers (
    firstName VARCHAR(32) NOT NULL,
    secondName VARCHAR(32),
    sirname VARCHAR(32),
    gender VARCHAR(32),
    dayOfBirth INTEGER,
    monthOfBirth INTEGER,
    yearOfBirth INTEGER,
    otherNotes TEXT,
    email VARCHAR(128),
    phoneNumber VARCHAR(32) NOT NULL,
    address VARCHAR(32),
    city VARCHAR(32),
    postcode VARCHAR(32),
    country VARCHAR(32)
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("customers table created!")

  _, err = pool.Exec(`CREATE TABLE cases (
    summary VARCHAR(80) NOT NULL,
    description TEXT,
    customer INTEGER NOT NULL,
    createdBy INTEGER NOT NULL,
    assignedTo INTEGER,
    priority VARCHAR(32) NOT NULL,

    dayOpened INTEGER NOT NULL,
    monthOpened INTEGER NOT NULL,
    yearOpened INTEGER NOT NULL,
    hourOpened INTEGER NOT NULL,
    minuteOpened INTEGER NOT NULL,
    secondOpened INTEGER NOT NULL,
    dayClosed INTEGER,
    monthClosed INTEGER,
    yearClosed INTEGER,
    hourClosed INTEGER,
    minuteClosed INTEGER,
    secondClosed INTEGER,
    FOREIGN KEY (customer)
      REFERENCES customers(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (createdBy)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (assignedTo)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("customers table created!")

  _, err = pool.Exec(`CREATE TABLE contacts (
    description TEXT NOT NULL,
    contactMethod VARCHAR(10) NOT NULL,
    caseId INTEGER,
    user INTEGER NOT NULL,

    day INTEGER NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    hour INTEGER NOT NULL,
    minute INTEGER NOT NULL,
    second INTEGER NOT NULL,
    FOREIGN KEY (caseId)
      REFERENCES cases(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (user)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("contacts table created!")

  _, err = pool.Exec(`CREATE TABLE subscriptions (
    name VARCHAR(32) NOT NULL,
    description TEXT,
    frequency VARCHAR(10) NOT NULL,
    price INTEGER NOT NULL
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("subscriptions table created!")

  _, err = pool.Exec(`CREATE TABLE subscriptionToCustomer (
    customer INTEGER NOT NULL,
    subscription INTEGER NOT NULL,
    dayStarted INTEGER NOT NULL,
    monthStarted INTEGER NOT NULL,
    yearStarted INTEGER NOT NULL,
    hourStarted INTEGER NOT NULL,
    minuteStarted INTEGER NOT NULL,
    secondStarted INTEGER NOT NULL,
    dayEnded INTEGER,
    monthEnded INTEGER,
    yearEnded INTEGER,
    hourEnded INTEGER,
    minuteEnded INTEGER,
    secondEnded INTEGER,
    FOREIGN KEY (customer)
      REFERENCES customers(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (subscription)
      REFERENCES subscriptions(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("subscriptionToCustomer table created!")

  _, err = pool.Exec(`CREATE TABLE bills (
    subsctiptionToCustomer INTEGER NOT NULL,
    paid INTEGER NOT NULL,

    dayPaid INTEGER NOT NULL,
    monthPaid INTEGER NOT NULL,
    yearPaid INTEGER NOT NULL,
    hourPaid INTEGER NOT NULL,
    minutePaid INTEGER NOT NULL,
    secondPaid INTEGER NOT NULL,
    FOREIGN KEY (subsctiptionToCustomer)
      REFERENCES subsctiptionToCustomer(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("subscriptions table created!")

  // _, err = pool.Exec(`CREATE TABLE wishedModelCars (
  //   modelCarId VARCHAR(64) NOT NULL,
  //   username VARCHAR(32) NOT NULL,
  //   wishFactor CHAR(10) NOT NULL,
  //   PRIMARY KEY (modelCarId, username)
  //   FOREIGN KEY (modelCarId)
  //     REFERENCES modelCars(id)
  //     ON DELETE CASCADE
  //     ON UPDATE CASCADE
  //   FOREIGN KEY (username)
  //     REFERENCES users(username)
  //     ON DELETE CASCADE
  //     ON UPDATE CASCADE
  // );`)
  // if (err != nil) { logError.Fatal(err.Error()) }
  // log.Print("ownedModelCars table created!")
}

func addTestData() {
  pool := openDatabase()
  
  testdata, err := os.ReadFile("./testdata.json")
  if (err != nil) {
    logError.Fatal("Failed to read file testdata.json")
  }
  var caseManagement CaseManagement
  err = json.Unmarshal(testdata, &caseManagement)
  if (err != nil) {
    logError.Fatal(err)
  }

  for userIndex := 0; userIndex < len(caseManagement.Users); userIndex++ {
    user := caseManagement.Users[userIndex]
    _, err = pool.Exec(`INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, ?);`, user.Username, user.Password, user.Name, user.Role)
    if (err != nil) { logError.Fatal(err.Error()) }

    // for ownedIndex := 0; ownedIndex < len(user.OwnedModelCars); ownedIndex++ {
    //   car := user.OwnedModelCars[ownedIndex]

    //   var modelCarId int;
    //   result := pool.QueryRow(`SELECT rowid FROM modelCars WHERE name=?;`, car.ModelCarName)
    //   err = result.Scan(&modelCarId)
    //   if (err != nil) { logError.Fatal(err.Error()) }
    //   log.Print("ModelCarId: ", modelCarId)

    //   _, err = pool.Exec(`INSERT INTO ownedModelCars (modelCarId, username, dateCollected) VALUES (?, ?, ?);`, modelCarId, user.Username, car.DateCollected)
    //   if (err != nil) { logError.Fatal(err.Error()) }
    // }
    // }
  }
  log.Print("Test data created for the users tables!")

  for customerIndex := 0; customerIndex < len(caseManagement.Customers); customerIndex++ {
    customer := caseManagement.Customers[customerIndex]
;
    _, err = pool.Exec(`
        INSERT INTO customers (
        firstName,
        secondName,
        sirname,
        gender,
        dayOfBirth,
        monthOfBirth,
        yearOfBirth,
        otherNotes,
        email,
        phoneNumber,
        address,
        city,
        postcode,
        country)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`,
        customer.FirstName,
        customer.SecondName,
        customer.Sirname,
        customer.Gender,
        customer.DayOfBirth,
        customer.MonthOfBirth,
        customer.YearOfBirth,
        customer.OtherNotes,
        customer.Email,
        customer.PhoneNumber,
        customer.Address,
        customer.City,
        customer.Postcode,
        customer.Country)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the customers tables!")

  for caseIndex := 0; caseIndex < len(caseManagement.Cases); caseIndex++ {
    caseRecord := caseManagement.Cases[caseIndex]
    _, err = pool.Exec(`
        INSERT INTO cases (
        summary,
        description,
        customer,
        createdBy,
        assignedTo,
        dayOpened,
        monthOpened,
        yearOpened,
        secondOpened,
        minuteOpened,
        hourOpened,
        dayClosed,
        monthClosed,
        yearClosed,
        secondClosed,
        minuteClosed,
        hourClosed,
        priority)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`,
        caseRecord.Summary,
        caseRecord.Description,
        caseRecord.Customer,
        caseRecord.CreatedBy,
        caseRecord.AssignedTo,
        caseRecord.DayOpened,
        caseRecord.MonthOpened,
        caseRecord.YearOpened,
        caseRecord.SecondOpened,
        caseRecord.MinuteOpened,
        caseRecord.HourOpened,
        caseRecord.DayClosed,
        caseRecord.MonthClosed,
        caseRecord.YearClosed,
        caseRecord.SecondClosed,
        caseRecord.MinuteClosed,
        caseRecord.HourClosed,
        caseRecord.Priority)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the cases tables!")

  for contactIndex := 0; contactIndex < len(caseManagement.Contacts); contactIndex++ {
    contact := caseManagement.Contacts[contactIndex]
    _, err = pool.Exec(`
        INSERT INTO contacts (
        description,
        day,
        month,
        year,
        second,
        minute,
        hour,
        contactMethod,
        caseId,
        user)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`,
        contact.Description,
        contact.Day,
        contact.Month,
        contact.Year,
        contact.Second,
        contact.Minute,
        contact.Hour,
        contact.ContactMethod,
        contact.Case,
        contact.User)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the contacts tables!")

  for subscriptionIndex := 0; subscriptionIndex < len(caseManagement.Subscriptions); subscriptionIndex++ {
    subscription := caseManagement.Subscriptions[subscriptionIndex]
    _, err = pool.Exec(`
        INSERT INTO subscriptions (
        name,
        description,
        frequency,
        price)
        VALUES (?, ?, ?, ?);`,
        subscription.Name,
        subscription.Description,
        subscription.Frequency,
        subscription.Price)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the subscriptions tables!")
}

type User struct {
  Username string
  Password string
  Name string
  Role string
  // OwnedModelCars []OwnedModelCar
}
type Customer struct {
  FirstName string
  SecondName string
  Sirname string
  Gender string
  DayOfBirth int32
  MonthOfBirth int32
  YearOfBirth int32
  OtherNotes string
  Email string;
  PhoneNumber string;
  Address string;
  City string;
  Postcode string;
  Country string;
}
type Case struct {
  Summary string
  Description string
  Customer int64
  CreatedBy int64
  AssignedTo int64
  DayOpened int32
  MonthOpened int32
  YearOpened int32
  SecondOpened int32
  MinuteOpened int32
  HourOpened int32
  DayClosed int32
  MonthClosed int32
  YearClosed int32
  SecondClosed int32
  MinuteClosed int32
  HourClosed int32
  Priority string
}
type Contact struct {
  Description string
  Day int32
  Month int32
  Year int32
  Second int32
  Minute int32
  Hour int32
  ContactMethod string
  Case int64
  User int64
}
type Subscription struct {
  Name string
  Description string
  Frequency string
  Price int
}
type SubscriptionToCustomer struct {
  Subscription int64
  Customer int64

  DayStarted int32
  MonthStarted int32
  YearStarted int32
  SecondStarted int32
  MinuteStarted int32
  HourStarted int32

  DayEnded int32
  MonthEnded int32
  YearEnded int32
  SecondEnded int32
  MinuteEnded int32
  HourEnded int32
}
type Bill struct {
  SubscriptionToCustomer int64
  paid int32
  DayPaid int32
  MonthPaid int32
  YearPaid int32
  SecondPaid int32
  MinutePaid int32
  HourPaid int32
}
type CaseManagement struct {
  Users []User
  Customers []Customer
  Cases []Case
  Contacts []Contact
  Subscriptions []Subscription
}

func (errorWriter ErrorWriter) Write(p []byte) (n int, err error) { // TODO: fix common code across modules
  color.Red(string(p))
  return 0, nil
}
type ErrorWriter struct {}
var logError *log.Logger

