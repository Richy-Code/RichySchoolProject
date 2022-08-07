package com.example.demo.services.service_interface;

import com.example.demo.entities.*;

import java.util.List;

public interface ClassesInterface {
   void saveClass(Classes classes);
   void saveAllClasses(List<Classes> classes);
   Classes findClassById(String id);
   List<Classes> classList();
   List<Classes> findClassByDepartment(Department department);
   List<String> findClassByTeacher(Long teacherId);
   List<Classes> listClassByDepartmentHead(DepartmentHead head);
   List<Classes> classByParentClass(ParentClass parentClass);
   void  deleteClass(Classes classes);

   void updateSubClassName(String name, String classId);

   List<Classes> teacherClasses(Long teacherId);
}
