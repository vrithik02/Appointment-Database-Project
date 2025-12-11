/********************************************************************************************
 *     InsuranceRepository
 *      The repository interface involving Insurance.
 *
 *      The imports of Repository and JpaRepository automatically provide the following prompts:
 *      - findAll() - Select * query
 *      - findById(Integer id) - select by primary key
 *      - save(Insurance entity) - insert and update
 *      - deleteById(Integer id) - remove
 *
 *      For simplistic queries, like findBy<variableTypeHere> only need their method headers,
 *      while more advance prompts require a different structure:
 *
 *      (Custom Query Format):
 *
 *      (@)Query       //(without the parenthesis around the @ sign)
 *      (""" ... """)  //(triple quotes ensures queries are clean and multi-lined)
 *
 *      List<Entity> findBy<...>(@Param("<databaseFieldName>") <modelFieldVarType> <modelFieldName>);
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 11/9/2025
 *
 ********************************************************************************************/
package com.appointmentProject.backend.repository;

import com.appointmentProject.backend.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Integer> {

    //Simplistic Additional Queries:

    List<Insurance> findByInsuranceName(String insurance_name);
    List<Insurance> findByPhone(String phoneNumber);
    List<Insurance> findByEmail(String email);
    List<Insurance> findByAddress(String address);

    //More Advanced Queries:



}
