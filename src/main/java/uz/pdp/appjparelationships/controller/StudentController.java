package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SubjectRepository subjectRepository;

//    CTRATE
    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto){
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()){
            return "Adress is not found";
        }
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "Group is not found";
        }
        List<Integer> subjectIds = studentDto.getSubjectIds();
        List<Subject> subjectslList = new ArrayList<>();
        for (Integer subjectId : subjectIds) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (!optionalSubject.isPresent()){
                return "Subject is not found";
            }
            subjectslList.add(optionalSubject.get());
        }
        boolean exists = studentRepository.existsByFirstNameAndLastNameAndGroupId(
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getGroupId());
        if (exists){
            return "This student is exist in this group";
        }
        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setSubjects(subjectslList);
        studentRepository.save(student);
        return "Student is added";

    }

//    READ
    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("'forFaculty/{facultyId}")
    public Page<Student> getStudentsForFaculty(@PathVariable Integer facultyId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPage;
    }

    //4. GROUP OWNER
    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentsForGroup(@PathVariable Integer groupId, @RequestParam int page){
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()){
            return new Student();
        }
        return optionalStudent.get();
    }

//    UPDATE
    @PutMapping("/{id}")
    public String editStudentById(@PathVariable Integer id, @RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (!optionalStudent.isPresent()){
            return "Student is not found";
        }
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()){
            return "Adress is not found";
        }
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "Group is not found";
        }
        List<Integer> subjectIds = studentDto.getSubjectIds();
        List<Subject> subjectslList = new ArrayList<>();
        for (Integer subjectId : subjectIds) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (!optionalSubject.isPresent()){
                return "Subject is not found";
            }
            subjectslList.add(optionalSubject.get());
        }
        boolean exists = studentRepository.existsByFirstNameAndLastNameAndGroupId(
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getGroupId());
        if (exists){
            return "This student is exist in this group";
        }
        Student student = optionalStudent.get();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setSubjects(subjectslList);
        studentRepository.save(student);
        return "Student is editeded";
    }

//    DELETE
    @DeleteMapping("/{id}")
    public String  deleteStudentById(@PathVariable Integer id){
        try {
            studentRepository.deleteById(id);
            return "Student is deleted";
        }catch (Exception e){
            return "Error";
        }
    }




}
