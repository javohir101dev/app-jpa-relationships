package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subject")
public class SubjectController {
    @Autowired
    SubjectRepository subjectRepository;

    //CREATE
    @RequestMapping(method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject) {
        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subjectRepository.save(subject);
        return "Subject added";
    }

    //READ
     @GetMapping
    public List<Subject> getSubjects() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList;
    }

//    READ for University
    @GetMapping("/byUniversityId/{universityId}")
    public List<Subject> getSubjectsForUniversity(@PathVariable Integer universityId){
        return subjectRepository.findAllByUniversityId(universityId);
    }

//    READ for Faculty
    @GetMapping("/byFacultyId/{facultyId}")
    public List<Subject> getSubjectsByFacltyId(@PathVariable Integer facultyId){
        return subjectRepository.findAllByFacultyId(facultyId);
    }
//    READ for Group
    @GetMapping("/byGroupId/{groupId}")
    public List<Subject> getSubjectsByGroupId(@PathVariable Integer groupId){
       return subjectRepository.findAllByGroupId(groupId);
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()){
            return new Subject();
        }
        return optionalSubject.get();
    }

//    UPDATE
    @PutMapping("/{id}")
    public String editSubjectByIdd(@PathVariable Integer id, Subject subject){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()){
            return "Subject is not found";
        }

        boolean existsByName = subjectRepository.existsByName(subject.getName());
        if (existsByName)
            return "This subject already exist";
        subject = optionalSubject.get();
        subjectRepository.save(subject);
        return "Subject edited";
    }

//    DELETE
    @DeleteMapping("/{id}")
    public String deleteSubjectById(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (!optionalSubject.isPresent()){
            return "Subject is not found";
        }
        subjectRepository.deleteById(id);
        return "Subject is deleted";
    }



}
