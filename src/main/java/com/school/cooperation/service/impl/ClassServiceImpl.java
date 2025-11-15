package com.school.cooperation.service.impl;

import com.school.cooperation.common.exception.BusinessException;
import com.school.cooperation.entity.EduClass;
import com.school.cooperation.entity.Student;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.ClassStatus;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.repository.EduClassRepository;
import com.school.cooperation.repository.StudentRepository;
import com.school.cooperation.repository.UserRepository;
import com.school.cooperation.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 班级管理服务实现类
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final EduClassRepository classRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;




    @Override
    @Transactional
    public EduClass createClass(EduClass eduClass) {
        // 检查班级名称是否已存在
        if (existsByName(eduClass.getName())) {
            throw new BusinessException(400, "班级名称已存在");
        }

        // 如果指定了班主任，检查是否存在且为教师角色
        if (eduClass.getTeacherId() != null) {
            User teacher = userRepository.findById(eduClass.getTeacherId())
                    .orElseThrow(() -> new BusinessException(404, "指定的班主任不存在"));

            if (!UserRole.TEACHER.equals(teacher.getRole())) {
                throw new BusinessException(400, "指定的用户不是教师角色");
            }
        }

        // 设置默认值
        eduClass.setStatus(ClassStatus.ACTIVE);
        // createdTime和updatedTime会通过JPA生命周期回调自动设置

        return classRepository.save(eduClass);
    }

    @Override
    @Transactional
    public EduClass updateClass(EduClass eduClass) {
        // 检查班级是否存在
        EduClass existingClass = classRepository.findById(eduClass.getId())
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        // 如果修改了班级名称，检查是否重复
        if (!existingClass.getName().equals(eduClass.getName()) && existsByName(eduClass.getName())) {
            throw new BusinessException(400, "班级名称已存在");
        }

        // 如果修改了班主任，检查是否存在且为教师角色
        if (eduClass.getTeacherId() != null && !eduClass.getTeacherId().equals(existingClass.getTeacherId())) {
            User teacher = userRepository.findById(eduClass.getTeacherId())
                    .orElseThrow(() -> new BusinessException(404, "指定的班主任不存在"));

            if (!UserRole.TEACHER.equals(teacher.getRole())) {
                throw new BusinessException(400, "指定的用户不是教师角色");
            }
        }

        // updatedTime会通过JPA生命周期回调自动设置

        return classRepository.save(eduClass);
    }

    @Override
    @Transactional
    public void updateClassStatus(Long classId, ClassStatus status) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        eduClass.setStatus(status);
        // updatedTime会通过JPA生命周期回调自动设置

        classRepository.save(eduClass);
    }

    @Override
    public List<EduClass> findByStatus(ClassStatus status) {
        return classRepository.findByStatus(status);
    }

    @Override
    public List<EduClass> findByTeacherId(Long teacherId) {
        return classRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<EduClass> findByGrade(Integer grade) {
        return classRepository.findByGrade(grade.toString());
    }

    @Override
    public List<EduClass> findAll() {
        return classRepository.findAll();
    }

    @Override
    public boolean existsByClassName(String className) {
        return false;
    }


    public boolean existsByName(String className) {
        return classRepository.existsByName(className);
    }

    @Override
    @Transactional
    public void assignTeacher(Long classId, Long teacherId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new BusinessException(404, "指定的班主任不存在"));

        if (!UserRole.TEACHER.equals(teacher.getRole())) {
            throw new BusinessException(400, "指定的用户不是教师角色");
        }

        eduClass.setTeacherId(teacherId);
        // updatedTime会通过JPA生命周期回调自动设置

        classRepository.save(eduClass);
    }

    @Override
    @Transactional
    public void removeTeacher(Long classId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        eduClass.setTeacherId(null);
        // updatedTime会通过JPA生命周期回调自动设置

        classRepository.save(eduClass);
    }

    @Override
    @Transactional
    public void addStudentToClass(Long classId, Long studentId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException(404, "学生不存在"));

        // 检查学生是否已经在班级中
        if (student.getClassId() != null && student.getClassId().equals(classId)) {
            throw new BusinessException(400, "学生已在班级中");
        }

        student.setClassId(classId);
        // updatedTime会通过JPA生命周期回调自动设置
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void removeStudentFromClass(Long classId, Long studentId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BusinessException(404, "学生不存在"));

        // 检查学生是否在指定班级中
        if (!classId.equals(student.getClassId())) {
            throw new BusinessException(400, "学生不在指定班级中");
        }

        student.setClassId(null);
        // updatedTime会通过JPA生命周期回调自动设置
        studentRepository.save(student);
    }

    @Override
    public List<Student> getStudentsByClass(Long classId) {
        return studentRepository.findByClassId(classId);
    }

    @Override
    public Optional<User> getTeacherByClass(Long classId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        if (eduClass.getTeacherId() != null) {
            return userRepository.findById(eduClass.getTeacherId());
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void addStudentsToClass(Long classId, List<Long> studentIds) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        for (Long studentId : studentIds) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new BusinessException(404, "学生ID " + studentId + " 不存在"));

            student.setClassId(classId);
            // updatedTime会通过JPA生命周期回调自动设置
            studentRepository.save(student);
        }
    }

    @Override
    @Transactional
    public void removeStudentsFromClass(Long classId, List<Long> studentIds) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        for (Long studentId : studentIds) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new BusinessException(404, "学生ID " + studentId + " 不存在"));

            if (classId.equals(student.getClassId())) {
                student.setClassId(null);
                // updatedTime会通过JPA生命周期回调自动设置
                studentRepository.save(student);
            }
        }
    }

    @Override
    @Transactional
    public void activateClass(Long classId) {
        updateClassStatus(classId, ClassStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateClass(Long classId) {
        updateClassStatus(classId, ClassStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void deleteClass(Long classId) {
        // 检查班级是否存在
        if (!classRepository.existsById(classId)) {
            throw new BusinessException(404, "班级不存在");
        }

        // 检查班级中是否有学生
        List<Student> students = studentRepository.findByClassId(classId);
        if (!students.isEmpty()) {
            throw new BusinessException(400, "班级中还有学生，无法删除");
        }

        classRepository.deleteById(classId);
    }

    @Override
    public Object getClassStatistics(Long classId) {
        EduClass eduClass = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException(404, "班级不存在"));

        List<Student> students = studentRepository.findByClassId(classId);

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("classId", eduClass.getId());
        statistics.put("className", eduClass.getName());
        statistics.put("grade", eduClass.getGrade());
        statistics.put("totalStudents", students.size());
        statistics.put("hasTeacher", eduClass.getTeacherId() != null);

        // 可以添加更多统计信息
        long maleCount = students.stream().filter(s -> "MALE".equals(s.getGender().name())).count();
        long femaleCount = students.stream().filter(s -> "FEMALE".equals(s.getGender().name())).count();

        Map<String, Long> genderStats = new HashMap<>();
        genderStats.put("male", maleCount);
        genderStats.put("female", femaleCount);
        statistics.put("genderStatistics", genderStats);

        return statistics;
    }
}