package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepositoryTest {

    private static final String EMAIL = "pedro@email.com";

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setup () {
        student = new Student("Pedro", EMAIL, Gender.MALE);
    }

    @AfterEach
    void after() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldSelectExistentEmail() {
        studentRepository.save(student);

        //when
        Boolean result = studentRepository.selectExistsEmail(EMAIL);

        //then
        assertTrue(result);

    }

    @Test
    void checkIfEmailExists() {

        //when
        Boolean result = studentRepository.selectExistsEmail(student.getEmail());

        //then
        assertFalse(result);

    }

}