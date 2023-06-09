package elwyn.case_management.views;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import elwyn.case_management.controllers.CaseController;
import elwyn.case_management.controllers.ContactController;
import elwyn.case_management.controllers.PerformanceController;
import elwyn.case_management.models.Case;
import elwyn.case_management.models.Contact;
import elwyn.case_management.models.Priority;
import elwyn.case_management.models.User;
import net.miginfocom.swing.MigLayout;

public class PerformanceView extends JScrollPane {
  PerformanceController performanceController;
  List<User> users;

  public PerformanceView(PerformanceController performanceController, List<User> users) {
    this.performanceController = performanceController;
    this.users = users;

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
  }

  public void display() {
    ContactController contactController = new ContactController(performanceController.user, null);
    CaseController caseController = new CaseController(performanceController.user, null);

    MigLayout mig = new MigLayout("wrap 3, alignx center");
    JPanel panel = new JPanel();
    panel.setLayout(mig);
    panel.setAlignmentX(CENTER_ALIGNMENT);
    for (User user : users) {
      List<Contact> contacts = contactController.readRecords(user.id);
      List<Case> cases = caseController.readRecords(user.id); // eTODO: fix: pagination

      JLabel titleLabel = new JLabel(user.name, SwingConstants.CENTER);
      titleLabel.setFont(new Font(getFont().getFontName(), Font.PLAIN, 30));
      titleLabel.setBorder(new EmptyBorder(new Insets(30, 0, 0, 0)));
      panel.add(titleLabel, "span, alignx center");
      panel.add(createContactMethodChart(contacts), "alignx center");
      panel.add(createPriorityChart(cases), "alignx center");
      panel.add(createContactsHandledChart(contacts), "alignx center");
    }

    setViewportView(panel);

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

  public JComponent createContactMethodChart(List<Contact> contacts) {
    int phoneCount = 0;
    int emailCount = 0;
    int postCount = 0;
    int otherCount = 0;
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

    PieChart contactMethodChart = new PieChartBuilder().width(300).height(300).title("Contact Method Breakdown").theme(ChartTheme.GGPlot2).build();
    contactMethodChart.getStyler().setLegendVisible(false);

    contactMethodChart.addSeries("Phone", phoneCount);
    contactMethodChart.addSeries("Email", emailCount);
    contactMethodChart.addSeries("Post", postCount);
    contactMethodChart.addSeries("Other", otherCount);
    JPanel contactMethodChartPanel = new XChartPanel<PieChart>(contactMethodChart);

    return contactMethodChartPanel;
  }

  public JComponent createPriorityChart(List<Case> cases) {
    int lowCount = 0;
    int mediumCount = 0;
    int highCount = 0;
    int urgentCount = 0;
    // List<Date> solvedCaseDates = new ArrayList<Date>();
    // List<int> solvedCaseDates = new ArrayList<Date>();
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



    PieChart priorityChart = new PieChartBuilder().width(300).height(300).title("Case Priority Breakdown").theme(ChartTheme.GGPlot2).build();
    priorityChart.getStyler().setLegendVisible(false);

    priorityChart.addSeries(Priority.LOW.toString(), lowCount);
    priorityChart.addSeries(Priority.MEDIUM.toString(), mediumCount);
    priorityChart.addSeries(Priority.HIGH.toString(), highCount);
    priorityChart.addSeries(Priority.URGENT.toString(), urgentCount);
    return new XChartPanel<PieChart>(priorityChart);
  }

  public JComponent createContactsHandledChart(List<Contact> contacts) {
    List<Date> contactDates = new ArrayList<Date>();
    List<Integer> contactCounts = new ArrayList<Integer>();

    if (contacts.size() > 0) {
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
    }
    else { // The user has not handled any contacts, show an empty graph. XChart requires the arrays to not be empty
      contactDates.add(new Date());
      contactCounts.add(0);
    }



    XYChart contactsHandledChart = new XYChartBuilder().width(500).height(300).title("Contacts Handled").theme(ChartTheme.GGPlot2).build();
    contactsHandledChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
    contactsHandledChart.getStyler().setLegendVisible(false);
    contactsHandledChart.addSeries("Contacts Handled", contactDates, contactCounts);
    return new XChartPanel<XYChart>(contactsHandledChart);
  }



  //*********** Utilities ***********//

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
    if (left.getDate() != right.getDate())
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

    if (left.getDate() < right.getDate())
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
}
