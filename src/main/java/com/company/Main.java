package com.company;

import com.company.jaxb.Employee;
import com.company.jaxb.Employees;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

  public static final String EMPLOYEES_XML_FILE_NAME = "Employees.xml";
  public static final String EMPLOYEES_JSON_FILE_NAME = "Employees.json";
  public static final String EMPLOYEES_BIN_FILE_NAME = "Employees.bin";
  public static final String EMPLOYEES_FBS_FILE_NAME = "Employees.fbs";
  public static final Path XML_FILE_PATH = Paths.get("target/classes/" + EMPLOYEES_XML_FILE_NAME);
  public static final Path JSON_FILE_PATH = Paths.get("target/classes/" + EMPLOYEES_JSON_FILE_NAME);
  public static final Path FLATBUF_BIN_FILE_PATH = Paths.get("target/classes/" + EMPLOYEES_BIN_FILE_NAME);



  /**
   * Edit this to generate larger files.
   */
  public static final int NUM_OF_EMPLOYEES = 1;

  /**
   * Edit this to erase/not erase generated files upon completion
   */
  public static final boolean CLEANUP = true;


  public static void main(String[] args) throws JAXBException, IOException, URISyntaxException {
    setup();

    checkJaxb();
    checkFlatBuf();

    if(CLEANUP) {
      deleteFiles();
    }
  }

  private static void setup() throws IOException {
    createXmlFile();
    createFlatBufFile();
  }

  private static void createXmlFile() throws IOException {

    Files.deleteIfExists(XML_FILE_PATH);
    Files.createFile(XML_FILE_PATH);

    String start = "<employees>\n";
    String content =
            "    <employee>\n" +
                    "        <name>Christian</name>\n" +
                    "        <age>35</age>\n" +
                    "    </employee>\n";
    String end = "</employees>";

    Files.write(XML_FILE_PATH, start.getBytes(), StandardOpenOption.CREATE);
    for(int i = 0; i < NUM_OF_EMPLOYEES; i++) {
      Files.write(XML_FILE_PATH, content.getBytes(), StandardOpenOption.APPEND);
    }
    Files.write(XML_FILE_PATH, end.getBytes(), StandardOpenOption.APPEND);
  }

  private static void createFlatBufFile() throws IOException {
    createJsonFile();

    try {
      generateFlatBufBinaryFromJson();
    } catch (Exception e) {
      System.out.println("Something went wrong, did you install flatc?");
      System.out.println("If you are on mac try: \"brew install flatc\"");
      System.out.println("...or have a look here:");
      System.out.println("http://google.github.io/flatbuffers/flatbuffers_guide_building.html");
      e.printStackTrace();
      System.exit(0);
    }
  }

  private static void createJsonFile() throws IOException {
    Files.deleteIfExists(JSON_FILE_PATH);
    Files.createFile(JSON_FILE_PATH);

    String start = "" +
            "{\n" +
            "    employees: [\n";
    String content =
            "    {\n" +
                    "      name: \"Christian\",\n" +
                    "      age: 35\n" +
                    "    },\n";
    String end =
            "  ]\n" +
                    "}";

    Files.write(JSON_FILE_PATH, start.getBytes(), StandardOpenOption.CREATE);
    for(int i = 0; i < NUM_OF_EMPLOYEES; i++) {
      Files.write(JSON_FILE_PATH, content.getBytes(), StandardOpenOption.APPEND);
    }
    Files.write(JSON_FILE_PATH, end.getBytes(), StandardOpenOption.APPEND);
  }

  private static void generateFlatBufBinaryFromJson() throws IOException {
    ProcessBuilder builder = new ProcessBuilder("flatc", "-b", EMPLOYEES_FBS_FILE_NAME, EMPLOYEES_JSON_FILE_NAME);
    builder.redirectErrorStream(true);
    ClassLoader classLoader = Main.class.getClassLoader();
    builder.directory(new File(classLoader.getResource(".").getFile()));
    Process p = builder.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;
    while (true) {
      line = r.readLine();
      if (line == null) { break; }
      System.out.println(line);
    }
  }

  private static void checkJaxb() throws JAXBException, IOException {
    ClassLoader classLoader = Main.class.getClassLoader();
    File file = new File(classLoader.getResource(EMPLOYEES_XML_FILE_NAME).getFile());

    JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    jaxbUnmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());

    long startTime = System.currentTimeMillis();
    Employees employees = (Employees) jaxbUnmarshaller.unmarshal(file);
    for (Employee employee: employees.getEmployees()) {
      employee.getName();
      employee.getName();
    }
    long stopTime = System.currentTimeMillis();

    printResult(
            file,
            stopTime - startTime,
            employees.getEmployees().size(),
            "JAXB");

  }

  private static void checkFlatBuf() throws IOException {
    ClassLoader classLoader = Main.class.getClassLoader();
    File file = new File(classLoader.getResource(EMPLOYEES_BIN_FILE_NAME).getFile());

    byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
    java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(bytes);

    long startTime = System.currentTimeMillis();
    com.company.flatbuf.Employees employees = com.company.flatbuf.Employees.getRootAsEmployees(buf);
    for (int i = 0; i < employees.employeesLength(); i++) {
      employees.employees(i).name();
      employees.employees(i).age();
    }
    long stopTime = System.currentTimeMillis();

    printResult(
            file,
            stopTime - startTime,
            employees.employeesLength(),
            "FlatBuf");
  }

  private static void printResult(File file, long elapsedTime, int numOfEmployees, String type) {
    System.out.println(type
            +": "
            + elapsedTime
            + " ms for a file of "
            + FileUtils.byteCountToDisplaySize(file.length())
            + " with "
            + numOfEmployees
            + " employees."
    );
  }

  private static void deleteFiles() throws IOException {
      Files.deleteIfExists(XML_FILE_PATH);
      Files.deleteIfExists(JSON_FILE_PATH);
      Files.deleteIfExists(FLATBUF_BIN_FILE_PATH);
  }
}
