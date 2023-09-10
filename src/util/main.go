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
  pool, err := sql.Open("sqlite3", "../../assets/clinic.db") // TODO: get the password from a file
  if (err != nil) { logError.Fatal(err.Error()) } // TODO: error handling

  pool.SetConnMaxLifetime(0)
  pool.SetMaxIdleConns(1)
  pool.SetMaxOpenConns(1)
  if err := pool.Ping(); err != nil { logError.Fatal(err.Error()) }

  return pool
}

func dropDatabase() {
  log.Print("Dropping clinic Database.")
  err := os.Remove("../../assets/clinic.db")
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
  );`)
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
    country VARCHAR(32),
    active VARCHAR(32),
    familyName VARCHAR(32),

    healthConditions TEXT,
    currentPrescriptions TEXT,
    allergies TEXT
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("customers table created!")

  _, err = pool.Exec(`CREATE TABLE appointments (
    summary VARCHAR(80) NOT NULL,
    description TEXT,
    pharmacy TEXT,
    customer INTEGER NOT NULL,
    assignedTo INTEGER,
    referredTo INTEGER,
    visitationStatus VARCHAR(32),
    prescribedHourlyFrequency INTEGER,
    prescribedMgDose INTEGER,
    prescribedMedication VARCHAR(32),
    closed VARCHAR(32),
    createdBy INTEGER NOT NULL,

    dayCreated INTEGER NOT NULL,
    monthCreated INTEGER NOT NULL,
    yearCreated INTEGER NOT NULL,
    hourCreated INTEGER NOT NULL,
    minuteCreated INTEGER NOT NULL,
    secondCreated INTEGER NOT NULL,

    day INTEGER NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    hour INTEGER NOT NULL,
    minute INTEGER NOT NULL,
    second INTEGER NOT NULL,
    FOREIGN KEY (assignedTo)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (referredTo)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (customer)
      REFERENCES customers(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (createdBy)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("appointments table created!")

  _, err = pool.Exec(`CREATE TABLE diseases (
    name VARCHAR(32) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(64) NOT NULL
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("diseases table created!")

  _, err = pool.Exec(`CREATE TABLE quoteOfTheDay (
    quote TEXT NOT NULL
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("quoteOfTheDay table created!")

  _, err = pool.Exec(`CREATE TABLE medicines (
    name VARCHAR(32) NOT NULL,
    description TEXT NOT NULL
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("medicines table created!")

  _, err = pool.Exec(`CREATE TABLE diseaseToMedicine (
    disease INTEGER NOT NULL,
    medicine INTEGER NOT NULL,
    FOREIGN KEY (disease)
      REFERENCES diseases(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
    FOREIGN KEY (medicine)
      REFERENCES medicines(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("subscriptionToCustomer table created!")

  _, err = pool.Exec(`CREATE TABLE logs (
    user INTEGER,
    log TEXT NOT NULL,
    severity VARCHAR(32) NOT NULL,
    day INTEGER NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    hour INTEGER NOT NULL,
    minute INTEGER NOT NULL,
    second INTEGER NOT NULL,
    FOREIGN KEY (user)
      REFERENCES users(rowid)
      ON DELETE CASCADE
      ON UPDATE CASCADE
  );`)
  if (err != nil) { logError.Fatal(err.Error()) }
  log.Print("logs table created!")
}

func addTestData() {
  pool := openDatabase()
  
  testData, err := os.ReadFile("./testData/testData.json")
  if (err != nil) {
    logError.Fatal("Failed to read file testData/testData.json")
  }
  var clinic Clinic
  err = json.Unmarshal(testData, &clinic)
  if (err != nil) {
    logError.Fatal(err)
  }

  for userIndex := 0; userIndex < len(clinic.Users); userIndex++ {
    user := clinic.Users[userIndex]
    _, err = pool.Exec(`INSERT INTO users (username, password, name, role) VALUES (?, ?, ?, ?);`, user.Username, user.Password, user.Name, user.Role)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the users tables!")

  for customerIndex := 0; customerIndex < len(clinic.Customers); customerIndex++ {
    customer := clinic.Customers[customerIndex]
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

  for appointmentIndex := 0; appointmentIndex < len(clinic.Appointments); appointmentIndex++ {
    appointmentRecord := clinic.Appointments[appointmentIndex]
    _, err = pool.Exec(`
        INSERT INTO appointments (
        summary,
        description,
        pharmacy,
        customer,
        createdBy,
        assignedTo,
        dayCreated,
        monthCreated,
        yearCreated,
        secondCreated,
        minuteCreated,
        hourCreated,
        day,
        month,
        year,
        second,
        minute,
        hour)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);`,
        appointmentRecord.Summary,
        appointmentRecord.Description,
        appointmentRecord.Pharmacy,
        appointmentRecord.Customer,
        appointmentRecord.CreatedBy,
        appointmentRecord.AssignedTo,
        appointmentRecord.DayCreated,
        appointmentRecord.MonthCreated,
        appointmentRecord.YearCreated,
        appointmentRecord.SecondCreated,
        appointmentRecord.MinuteCreated,
        appointmentRecord.HourCreated,
        appointmentRecord.Day,
        appointmentRecord.Month,
        appointmentRecord.Year,
        appointmentRecord.Second,
        appointmentRecord.Minute,
        appointmentRecord.Hour)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the appointments tables!")

  for diseaseIndex := 0; diseaseIndex < len(clinic.Diseases); diseaseIndex++ {
    disease := clinic.Diseases[diseaseIndex]
    _, err = pool.Exec(`
        INSERT INTO diseases (
        name,
        description,
        category)
        VALUES (?, ?, ?);`,
        disease.Name,
        disease.Description,
        disease.Category)
    if (err != nil) { logError.Fatal(err.Error()) }
    if diseaseIndex % 500 == 0 {
      log.Print(diseaseIndex)
    }
  }
  log.Print("Test data created for the diseases tables!")

  for medicineIndex := 0; medicineIndex < len(clinic.Medicines); medicineIndex++ {
    medicine := clinic.Medicines[medicineIndex]
    _, err = pool.Exec(`
        INSERT INTO medicines (
        name,
        description)
        VALUES (?, ?);`,
        medicine.Name,
        medicine.Description)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the medicines tables!")

  for logIndex := 0; logIndex < len(clinic.Logs); logIndex++ {
    log := clinic.Logs[logIndex]
    _, err = pool.Exec(`
        INSERT INTO logs (
        user,
        severity,
        log,
        day,
        month,
        year,
        second,
        minute,
        hour)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);`,
        log.User,
        log.Severity,
        log.Log,
        log.Day,
        log.Month,
        log.Year,
        log.Second,
        log.Minute,
        log.Hour)
    if (err != nil) { logError.Fatal(err.Error()) }
  }
  log.Print("Test data created for the logs tables!")
}

type User struct {
  Username string
  Password string
  Name string
  Role string
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

type Appointment struct {
  Summary string
  Description string
  Pharmacy string
  Customer int64
  CreatedBy int64
  AssignedTo int64
  DayCreated int32
  MonthCreated int32
  YearCreated int32
  SecondCreated int32
  MinuteCreated int32
  HourCreated int32
  Day int32
  Month int32
  Year int32
  Second int32
  Minute int32
  Hour int32
}

type Disease struct {
  Name string
  Description string
  Category string
}

type Medicine struct {
  Name string
  Description string
}

type DiseaseToMedicine struct {
  Disease int64
  Medicine int64
}

type Log struct {
  User int64
  Severity string
  Log string
  Day int32
  Month int32
  Year int32
  Second int32
  Minute int32
  Hour int32
} 

type Clinic struct {
  Users []User
  Customers []Customer
  Appointments []Appointment
  Diseases []Disease
  Medicines []Medicine
  Logs []Log
}

func (errorWriter ErrorWriter) Write(p []byte) (n int, err error) { // TODO: fix common code across modules
  color.Red(string(p))
  return 0, nil
}

type ErrorWriter struct {}
var logError *log.Logger

