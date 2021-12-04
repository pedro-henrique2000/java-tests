package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void testGetAllStudentsFlow() {
        studentService.getAllStudents();

        verify(studentRepository, atMostOnce()).findAll();
    }

    @Test
    void testGetStudents() {
        List<Student> students =
                List.of(new Student("Pedro","1",Gender.MALE), new Student("K", "1", Gender.FEMALE));

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> studentList = studentService.getAllStudents();

        assertEquals(students.size(), studentList.size());
    }

    @Test
    void canAddStudent() {
        Student student = new Student("Pedro", "M", Gender.MALE);

        studentService.addStudent(student);

        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(), student);
    }

    @Test
    void throwBadRequest() {
        Student student = new Student();
        when(studentRepository.selectExistsEmail(any())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            studentService.addStudent(student);
        });

        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent() {
        when(studentRepository.existsById(any())).thenReturn(true);

        studentService.deleteStudent(any());

        verify(studentRepository, atMostOnce()).deleteById(any());
    }

    @Test
    void notDelete() {
        when(studentRepository.existsById(any())).thenReturn(false);

        assertThrows(StudentNotFoundException.class, () -> {
            studentService.deleteStudent(any());
        });

        verify(studentRepository, never()).deleteById(any());
    }
}