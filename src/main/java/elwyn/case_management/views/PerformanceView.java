package elwyn.case_management.views;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;

import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.awt.event.*;
import java.time.Clock;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;
import elwyn.case_management.controllers.PerformanceController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.Priority;

public class PerformanceView extends JScrollPane {
  PerformanceController performanceController;

  public PerformanceView(PerformanceController performanceController) {
    this.performanceController = performanceController;
    display();
  }

  public static int dateValue(Date date) {
    int yearValue = date.getYear() * 12;
    int monthValue = date.getMonth() * 31;
    int dayValue = date.getDate();
    return yearValue + monthValue + dayValue;
  }

  public static boolean dateEquals(Date left, Date right) {
    if (left.getYear() != right.getYear())
      return false;
    if (left.getMonth() != right.getMonth())
      return false;
    if (left.getDay() != right.getDay())
      return false;
    return true;
  }
 // eTODO: https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal
 // eTODO: Change the comparison functions to give each date a single value. makes debugging easier too (eg. a year has a value of 12)

  public static boolean dateLessThan(Date left, Date right) {
    if (left.getYear() < right.getYear())
      return true;
    if (left.getYear() > right.getYear())
      return false;

    if (left.getMonth() < right.getMonth())
      return true;
    if (left.getMonth() > right.getMonth())
      return false;

    if (left.getDay() < right.getDay())
      return true;
    return false;
  }

  public static void swap(List<Contact> contacts, int i, int j) {
    Contact tmp = contacts.get(j);
    contacts.set(j, contacts.get(i));
    contacts.set(i, tmp);
  }

  public static int partition(List<Contact> contacts, int left, int right) {
    Contact pivot = contacts.get(left);
    int leftWall = left;

    for (int i = leftWall + 1; i < contacts.size(); i++) {
      if (dateValue(contacts.get(i).date) < dateValue(pivot.date)) {
        leftWall++;
        swap(contacts, leftWall, i);
      }
    }
    swap(contacts, left, leftWall);
    return leftWall;
  }

  public static void quickSort(List<Contact> contacts, int left, int right) {
    if (left < right) {
      int pivotLocation = partition(contacts, left, right);
      quickSort(contacts, left, pivotLocation - 1);
      quickSort(contacts, pivotLocation + 1, right);
    }
  }

  public void display() {
    ContactController contactController = new ContactController(performanceController::selectMyContacts);
    int phoneCount = 0;
    int emailCount = 0;
    int postCount = 0;
    int otherCount = 0;
    List<Date> contactDates = new ArrayList<Date>();
    List<Integer> contactCounts = new ArrayList<Integer>();

    List<Contact> contacts = contactController.readRecords(performanceController.user.id);

    // quickSort(contacts, 0, contacts.size());
    contacts.sort(new Comparator<Contact>() {
      public int compare(Contact o1, Contact o2) {
        int o1Value = o1.date.getYear() * 12 + o1.date.getMonth() * 31 + o1.date.getDate();
        int o2Value = o2.date.getYear() * 12 + o2.date.getMonth() * 31 + o2.date.getDate();
        return o1Value - o2Value;
      }
    });

    Date lastDate = contacts.get(0).date;
    contactDates.add(lastDate);
    contactCounts.add(1);

    for (int i = 1; i < contacts.size(); i++) {
      if (dateEquals(lastDate, contacts.get(i).date)) {
        int countsIndex = contactCounts.size() - 1;
        int count = contactCounts.get(countsIndex);
        contactCounts.set(countsIndex, ++count);
        continue;
      }
      lastDate = contacts.get(i).date;
      contactDates.add(lastDate);
      contactCounts.add(1);
    }

    for (Contact contact : contacts) {
      switch (contact.contactMethod) {
        case PHONE:
          phoneCount++;
          break;
        case EMAIL:
          emailCount++;
          break;
        case POST:
          postCount++;
          break;
        case OTHER:
          otherCount++;
          break;
      }
    }
    int lowCount = 0;
    int mediumCount = 0;
    int highCount = 0;
    int urgentCount = 0;
    // List<Date> solvedCaseDates = new ArrayList<Date>();
    // List<int> solvedCaseDates = new ArrayList<Date>();

    CaseController caseController = new CaseController(performanceController::selectMyCases);
    List<Case> cases = caseController.readRecords();
    for (int i = 0; i < cases.size(); i++) {
      Case caseRecord = cases.get(i);
      switch (caseRecord.priority) {
        case LOW:
          lowCount++;
          break;
        case MEDIUM:
          mediumCount++;
          break;
        case HIGH:
          highCount++;
          break;
        case URGENT:
          urgentCount++;
          break;
      }
      // boolean caseClosed = !caseRecord.dateClosed.equals("") && caseRecord.dateClosed != null;
      // if (caseClosed) {
      //   SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
      //   solvedCaseDates.add(i, df.parse(caseRecord.dateClosed));
      // }
    }

    PieChart contactMethodChart = new PieChartBuilder().width(300).height(300).title("Contact Method Breakdown").theme(ChartTheme.GGPlot2).build();
    contactMethodChart.getStyler().setLegendVisible(false);

    contactMethodChart.addSeries("Phone", phoneCount);
    contactMethodChart.addSeries("Email", emailCount);
    contactMethodChart.addSeries("Post", postCount);
    contactMethodChart.addSeries("Other", otherCount);
    JPanel contactMethodChartPanel = new XChartPanel<PieChart>(contactMethodChart);



    PieChart priorityChart = new PieChartBuilder().width(300).height(300).title("Case Priority Breakdown").theme(ChartTheme.GGPlot2).build();
    priorityChart.getStyler().setLegendVisible(false);

    priorityChart.addSeries(Priority.LOW.toString(), lowCount);
    priorityChart.addSeries(Priority.MEDIUM.toString(), mediumCount);
    priorityChart.addSeries(Priority.HIGH.toString(), highCount);
    priorityChart.addSeries(Priority.URGENT.toString(), urgentCount);
    JPanel priorityChartPanel = new XChartPanel<PieChart>(priorityChart);



    XYChart contactsHandledChart = new XYChartBuilder().width(500).height(300).title("Contacts Handled").theme(ChartTheme.GGPlot2).build();
    contactsHandledChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    contactsHandledChart.getStyler().setLegendVisible(false);
    contactsHandledChart.addSeries("Contacts Handled", contactDates, contactCounts);
    JPanel contactsHandledPanel = new XChartPanel<XYChart>(contactsHandledChart);



    JPanel panel = new JPanel();
    panel.add(contactMethodChartPanel);
    panel.add(priorityChartPanel);
    panel.add(contactsHandledPanel);

    setViewportView(panel);

    addComponentListener(new ComponentListener() { // This must be done to handle databases changes that could happen in other tabs
        @Override
        public void componentShown(ComponentEvent e) {
          display();
        }
        @Override
        public void componentResized(ComponentEvent e) {}
        @Override
        public void componentMoved(ComponentEvent e) {}
        @Override
        public void componentHidden(ComponentEvent e) {}
    });
    //    open cases and a listing of open VIP cases
    //    make cases hold a list of users and have a collaboration field shown here?
    //    high priority or vip cases?
    // cases handled - week, month, total - XYChart
    // contacts handled - week, month, total - XYChart
    // percent breakdown of contact methods - week, month, total
    // percent breakdown of priority - week, month, total
    //    maybe also add a customer satisfaction field and do a breakdown of this too
    // customers handled - week, month, total - XYChart
    // vip customers handled - week, month, total - Text Field
    // subscriptions?
  }
}

