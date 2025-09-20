package com.example.project2.service;

import com.example.project2.model.Group;
import com.example.project2.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id)
                .filter(group -> !group.getIsDeleted());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Group> findAll() {
        return groupRepository.findByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findByIsDeletedFalse(pageable);
    }
    
    @Override
    public Group update(Long id, Group group) {
        return groupRepository.findById(id)
                .map(existingGroup -> {
                    if (existingGroup.getIsDeleted()) {
                        throw new RuntimeException("Группа удалена и не может быть изменена");
                    }
                    existingGroup.setName(group.getName());
                    existingGroup.setDescription(group.getDescription());
                    existingGroup.setCourseYear(group.getCourseYear());
                    return groupRepository.save(existingGroup);
                })
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));
    }
    
    @Override
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
    
    @Override
    public void softDeleteById(Long id) {
        groupRepository.findById(id)
                .ifPresent(group -> {
                    group.setIsDeleted(true);
                    groupRepository.save(group);
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Group> findBySearchTerm(String search, Pageable pageable) {
        return groupRepository.findBySearchTerm(search, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Group> findByCourseYear(Integer courseYear) {
        return groupRepository.findByCourseYearAndIsDeletedFalse(courseYear);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Group> findByCourseYear(Integer courseYear, Pageable pageable) {
        return groupRepository.findByCourseYearAndIsDeletedFalse(courseYear, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return groupRepository.countByIsDeletedFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByCourseYear(Integer courseYear) {
        return groupRepository.countByCourseYearAndIsDeletedFalse(courseYear);
    }
    
    @Override
    public void restoreById(Long id) {
        groupRepository.findById(id)
                .ifPresent(group -> {
                    group.setIsDeleted(false);
                    groupRepository.save(group);
                });
    }
}
