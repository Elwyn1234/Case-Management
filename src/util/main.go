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
    dateOfBirth CHAR(10),
    otherNotes TEXT,
    email VARCHAR(32),
    phoneNumber VARCHAR(32) NOT NULL,
    addressLine1 VARCHAR(32),
    addressLine2 VARCHAR(32),
    addressLine3 VARCHAR(32),
    addressLine4 VARCHAR(32),
    postcode VARCHAR(32),
    country VARCHAR(32)
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("customers table created!")

  _, err = pool.Exec(`CREATE TABLE cases (
    summary VARCHAR(80) NOT NULL,
    description TEXT,
    customer INTEGER NOT NULL,
    dateOpened CHAR(10) NOT NULL,
    dateClosed CHAR(10),
    PRIORITY VARCHAR(32) NOT NULL,
    FOREIGN KEY (customer)
      REFERENCES customers(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("customers table created!")

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
    _, err = pool.Exec(`INSERT INTO customers (firstName, secondName, sirname, gender, dateOfBirth, otherNotes, email, phoneNumber, addressLine1, addressLine2, addressLine3, addressLine4, postcode, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`, customer.FirstName, customer.SecondName, customer.Sirname, customer.Gender, customer.DateOfBirth, customer.OtherNotes, customer.email, customer.phoneNumber, customer.addressLine1, customer.addressLine2, customer.addressLine3, customer.addressLine4, customer.postcode, customer.country)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the customers tables!")

  for caseIndex := 0; caseIndex < len(caseManagement.Cases); caseIndex++ {
    caseRecord := caseManagement.Cases[caseIndex]
    _, err = pool.Exec(`INSERT INTO cases (summary, description, customer, dateOpened, dateClosed, priority) VALUES (?, ?, ?, ?, ?, ?);`, caseRecord.Summary, caseRecord.Description, caseRecord.Customer, caseRecord.DateOpened, caseRecord.DateClosed, caseRecord.Priority)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the cases tables!")
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
  DateOfBirth string
  OtherNotes string
  email string;
  phoneNumber string;
  addressLine1 string;
  addressLine2 string;
  addressLine3 string;
  addressLine4 string;
  postcode string;
  country string;
}
type Case struct {
  Summary string
  Description string
  Customer string
  DateOpened string
  DateClosed string
  Priority string
}
type CaseManagement struct {
  Users []User
  Customers []Customer
  Cases []Case
}

func (errorWriter ErrorWriter) Write(p []byte) (n int, err error) { // TODO: fix common code across modules
  color.Red(string(p))
  return 0, nil
}
type ErrorWriter struct {}
var logError *log.Logger

