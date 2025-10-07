/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.schoolmanagementsystem;



import java.io.BufferedReader;


import java.io.FileReader;
import java.io.IOException;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author antal
 */

// Subject.java
enum Subject {
    SUBJECT1("Subject1"),
    SUBJECT2("Subject2"),
    SUBJECT3("Subject3"),
    SUBJECT4("Subject4"),
    SUBJECT5("Subject5"),
    SUBJECT6("Subject6"),
    SUBJECT7("Subject7"),
    SUBJECT8("Subject8"),
    SUBJECT9("Subject9"),
    SUBJECT10("Subject10");
    
    private String name;
    
    Subject(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

// TimeSlot.java
class TimeSlot {
    private int hour;
    private Subject subject;
    private Teacher teacher;
    
    public TimeSlot(int hour, Subject subject, Teacher teacher) {
        this.hour = hour;
        this.subject = subject;
        this.teacher = teacher;
    }
    
    public int getHour() {
        return hour;
    }
    
    public Subject getSubject() {
        return subject;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    @Override
    public String toString() {
        return String.format("%d:00 - %s (Teacher: %s)", 
            hour, subject.getName(), teacher.getName());
    }
}

// Timetable.java
/*import java.util.ArrayList;
import java.util.List;*/

class Timetable {
    private List<TimeSlot> schedule;
    
    public Timetable() {
        schedule = new ArrayList<>();
    }
    
    public void addTimeSlot(TimeSlot slot) {
        schedule.add(slot);
    }
    
    public List<TimeSlot> getSchedule() {
        return schedule;
    }
    
    public void displayTimetable() {
        System.out.println("\nDaily Schedule:");
        System.out.println("================");
        for (TimeSlot slot : schedule) {
            System.out.println(slot);
        }
    }
}

// Teacher.java
/*import java.util.ArrayList;
import java.util.List;*/

class Teacher {
    private int id;
    private String name;
    private Subject specialization;
    private List<String> assignedClassrooms;
    
    public Teacher(int id, String name, Subject specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.assignedClassrooms = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Subject getSpecialization() {
        return specialization;
    }
    
    public void assignClassroom(String classroomId) {
        if (!assignedClassrooms.contains(classroomId)) {
            assignedClassrooms.add(classroomId);
        }
    }
    
    public void displayInfo() {
        System.out.println("Teacher ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Specialization: " + specialization.getName());
        System.out.println("Assigned Classrooms: " + assignedClassrooms);
    }
}

// Student.java
class Student {
    private String name;
    private int studentId;
    private String assignedClassroom;
    
    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void assignToClassroom(String classroomId) {
        this.assignedClassroom = classroomId;
    }
    
    public String getAssignedClassroom() {
        return assignedClassroom;
    }
    
    @Override
    public String toString() {
        return String.format("Student #%d: %s (Classroom: %s)", 
            studentId, name, assignedClassroom);
    }
}

// Classroom.java
/*import java.util.ArrayList;
import java.util.List;*/

class Classroom {
    private String classroomId;
    private Timetable timetable;
    private List<Student> students;
    private static final int MAX_STUDENTS = 5;
    
    public Classroom(String classroomId) {
        this.classroomId = classroomId;
        this.timetable = new Timetable();
        this.students = new ArrayList<>();
    }
    
    public String getClassroomId() {
        return classroomId;
    }
    
    public Timetable getTimetable() {
        return timetable;
    }
    
    public boolean addStudent(Student student) {
        if (students.size() < MAX_STUDENTS) {
            students.add(student);
            student.assignToClassroom(classroomId);
            return true;
        }
        return false;
    }
    
    public List<Student> getStudents() {
        return students;
    }
    
    public void displayClassroomInfo() {
        System.out.println("\n========================================");
        System.out.println("Classroom: " + classroomId);
        System.out.println("========================================");
        System.out.println("Number of Students: " + students.size());
        System.out.println("\nStudents:");
        for (Student student : students) {
            System.out.println("  - " + student.getName());
        }
        timetable.displayTimetable();
    }
}

// JSONReader.java

class JSONReader {
    
    public JSONReader() {
    }
    
    private String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line.trim());
            }
        }
        return content.toString();
    }
    
    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(searchKey);
        if (keyIndex == -1) return null;
        
        int colonIndex = json.indexOf(":", keyIndex);
        int startIndex = colonIndex + 1;
        
        // Skip whitespace
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }
        
        if (json.charAt(startIndex) == '"') {
            // String value
            startIndex++;
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        } else if (json.charAt(startIndex) == '[') {
            // Array value
            int bracketCount = 1;
            int endIndex = startIndex + 1;
            while (bracketCount > 0 && endIndex < json.length()) {
                if (json.charAt(endIndex) == '[') bracketCount++;
                if (json.charAt(endIndex) == ']') bracketCount--;
                endIndex++;
            }
            return json.substring(startIndex, endIndex);
        } else {
            // Number value
            int endIndex = startIndex;
            while (endIndex < json.length() && (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '.')) {
                endIndex++;
            }
            return json.substring(startIndex, endIndex);
        }
    }
    
    private List<String> splitJsonArray(String arrayContent) {
        List<String> elements = new ArrayList<>();
        if (arrayContent.startsWith("[")) arrayContent = arrayContent.substring(1);
        if (arrayContent.endsWith("]")) arrayContent = arrayContent.substring(0, arrayContent.length() - 1);
        
        int braceCount = 0;
        int bracketCount = 0;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < arrayContent.length(); i++) {
            char c = arrayContent.charAt(i);
            
            if (c == '{') braceCount++;
            if (c == '}') braceCount--;
            if (c == '[') bracketCount++;
            if (c == ']') bracketCount--;
            
            if (c == ',' && braceCount == 0 && bracketCount == 0) {
                elements.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            elements.add(current.toString().trim());
        }
        
        return elements;
    }
    
    public List<Teacher> loadTeachers(String filename) {
        List<Teacher> teachers = new ArrayList<>();
        
        try {
            String content = readFile(filename);
            String teachersArray = extractValue(content, "teachers");
            
            List<String> teacherObjects = splitJsonArray(teachersArray);
            
            for (String teacherObj : teacherObjects) {
                String idStr = extractValue(teacherObj, "id");
                String name = extractValue(teacherObj, "name");
                String specialization = extractValue(teacherObj, "specialization");
                
                if (idStr != null && name != null && specialization != null) {
                    int id = Integer.parseInt(idStr);
                    teachers.add(new Teacher(id, name, Subject.valueOf(specialization)));
                }
            }
            
            System.out.println("✓ Loaded " + teachers.size() + " teachers from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading teachers: " + e.getMessage());
        }
        
        return teachers;
    }
    
    public List<Student> loadStudents(String filename) {
        List<Student> students = new ArrayList<>();
        
        try {
            String content = readFile(filename);
            String studentsArray = extractValue(content, "students");
            
            List<String> studentObjects = splitJsonArray(studentsArray);
            
            for (String studentObj : studentObjects) {
                String idStr = extractValue(studentObj, "studentId");
                String name = extractValue(studentObj, "name");
                
                if (idStr != null && name != null) {
                    int studentId = Integer.parseInt(idStr);
                    students.add(new Student(name, studentId));
                }
            }
            
            System.out.println("✓ Loaded " + students.size() + " students from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        
        return students;
    }
    
    public List<Classroom> loadClassrooms(String filename, Map<Integer, Teacher> teacherMap, 
                                          Map<Integer, Student> studentMap) {
        List<Classroom> classrooms = new ArrayList<>();
        
        try {
            String content = readFile(filename);
            String classroomsArray = extractValue(content, "classrooms");
            
            List<String> classroomObjects = splitJsonArray(classroomsArray);
            
            for (String classroomObj : classroomObjects) {
                String classroomId = extractValue(classroomObj, "classroomId");
                Classroom classroom = new Classroom(classroomId);
                
                // Add students to classroom
                String studentIdsStr = extractValue(classroomObj, "studentIds");
                List<String> studentIds = splitJsonArray(studentIdsStr);
                
                for (String idStr : studentIds) {
                    int studentId = Integer.parseInt(idStr.trim());
                    Student student = studentMap.get(studentId);
                    if (student != null) {
                        classroom.addStudent(student);
                    }
                }
                
                // Create timetable
                String timetableStr = extractValue(classroomObj, "timetable");
                List<String> timeSlots = splitJsonArray(timetableStr);
                
                for (String timeSlotStr : timeSlots) {
                    String hourStr = extractValue(timeSlotStr, "hour");
                    String subjectStr = extractValue(timeSlotStr, "subject");
                    String teacherIdStr = extractValue(timeSlotStr, "teacherId");
                    
                    if (hourStr != null && subjectStr != null && teacherIdStr != null) {
                        int hour = Integer.parseInt(hourStr);
                        Subject subject = Subject.valueOf(subjectStr);
                        int teacherId = Integer.parseInt(teacherIdStr);
                        Teacher teacher = teacherMap.get(teacherId);
                        
                        if (teacher != null) {
                            TimeSlot slot = new TimeSlot(hour, subject, teacher);
                            classroom.getTimetable().addTimeSlot(slot);
                            teacher.assignClassroom(classroomId);
                        }
                    }
                }
                
                classrooms.add(classroom);
            }
            
            System.out.println("✓ Loaded " + classrooms.size() + " classrooms from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading classrooms: " + e.getMessage());
        }
        
        return classrooms;
    }
}

// SchoolManagementSystem.java


public class SchoolManagementSystem {
    private List<Classroom> classrooms;
    private List<Teacher> teachers;
    private List<Student> students;
    private JSONReader jsonReader;
    
    public SchoolManagementSystem() {
        classrooms = new ArrayList<>();
        teachers = new ArrayList<>();
        students = new ArrayList<>();
        jsonReader = new JSONReader();
        
        loadDataFromJSON();
    }
    
    private void loadDataFromJSON() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      LOADING DATA FROM JSON FILES      ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Load teachers
        teachers = jsonReader.loadTeachers("teachers.json");
        
        // Load students
        students = jsonReader.loadStudents("students.json");
        
        // Create maps for easy lookup
        Map<Integer, Teacher> teacherMap = new HashMap<>();
        for (Teacher teacher : teachers) {
            teacherMap.put(teacher.getId(), teacher);
        }
        
        Map<Integer, Student> studentMap = new HashMap<>();
        for (Student student : students) {
            studentMap.put(student.getStudentId(), student);
        }
        
        // Load classrooms with timetables
        classrooms = jsonReader.loadClassrooms("classrooms.json", teacherMap, studentMap);
        
        System.out.println("\n✓ All data loaded successfully!\n");
    }
    
    public void displayAllClassrooms() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   SCHOOL MANAGEMENT SYSTEM - REPORT    ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        for (Classroom classroom : classrooms) {
            classroom.displayClassroomInfo();
        }
    }
    
    public void displayAllTeachers() {
        System.out.println("\n========================================");
        System.out.println("         ALL TEACHERS");
        System.out.println("========================================");
        for (Teacher teacher : teachers) {
            teacher.displayInfo();
            System.out.println("----------------------------------------");
        }
    }
    
    public void displayAllStudents() {
        System.out.println("\n========================================");
        System.out.println("         ALL STUDENTS");
        System.out.println("========================================");
        for (Student student : students) {
            System.out.println(student);
        }
    }
    
    public static void main(String[] args) {
        SchoolManagementSystem school = new SchoolManagementSystem();
        
        // Display all information
        school.displayAllClassrooms();
        school.displayAllTeachers();
        school.displayAllStudents();
    }
}

