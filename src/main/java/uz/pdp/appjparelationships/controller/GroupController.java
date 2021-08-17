package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Faculty;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.payload.GroupDto;
import uz.pdp.appjparelationships.repository.FacultyRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FacultyRepository facultyRepository;

//    CREATE
    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto){
        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()){
            return "Faculty is not found";
        }
        boolean exists = groupRepository.existsByNameAndFacultyId(groupDto.getName(), groupDto.getFacultyId());
        if (exists){
            return "This group is added to this faculty";
        }
        Group group = new Group();
        group.setFaculty(optionalFaculty.get());
        group.setName(groupDto.getName());
        groupRepository.save(group);
        return "Group is added";
    }

    //READ

    //VAZIRLIK UCHUN
    @GetMapping
    public List<Group> getGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups;
    }

    //UNIVERSITET MAS'UL XODIMI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId) {
        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(universityId);
        List<Group> groupsByUniversityId = groupRepository.getGroupsByUniversityId(universityId);
        List<Group> groupsByUniversityIdNative = groupRepository.getGroupsByUniversityIdNative(universityId);
        return allByFaculty_universityId;
    }

    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable Integer id){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()){
            return new Group();
        }
        return optionalGroup.get();
    }

//    UPDATE
    @PutMapping("/{id}")
    public String egitGroupById(@PathVariable Integer id, @RequestBody GroupDto groupDto){
        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (!optionalGroup.isPresent()){
            return "Group is not found";
        }
        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (!optionalFaculty.isPresent()){
            return "Faculty is not found";
        }
        boolean exists = groupRepository.existsByNameAndFacultyId(groupDto.getName(), groupDto.getFacultyId());
        if (exists){
            return "This group is added to this faculty";
        }
        Group group = optionalGroup.get();
        group.setFaculty(optionalFaculty.get());
        group.setName(groupDto.getName());
        groupRepository.save(group);
        return "Group is edited";
    }

//    DELETE
    @DeleteMapping("/{id}")
    public String deleteGroupById(@PathVariable Integer id){
        try {
            groupRepository.deleteById(id);
            return "Group is deleted";
        }catch (Exception e){
            return "Error";
        }
    }


}
