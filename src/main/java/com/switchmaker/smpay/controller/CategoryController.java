/**
 * @author: Aichatou
 * @version 1.0
 * @year: 2024
 * @since: 12:17
 * @Date: 15/01/2024
 */
package com.switchmaker.smpay.controller;

import com.switchmaker.smpay.entities.Category;
import com.switchmaker.smpay.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.UUID;

import static com.switchmaker.smpay.constant.urls.RootUrl.ROOT_API;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.ADMIN;
import static com.switchmaker.smpay.constant.values.GlobalConstantsValues.MANAGER;

@CrossOrigin("*")
@RestController
@RequestMapping(ROOT_API+"/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    	/*----------------------------------------/
		/*        Ajoute une categorie           /
		/*-------------------------------------*/

    @RolesAllowed({ADMIN,MANAGER})
    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody Category category){
        try {
            return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.OK);
        }catch (DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage();
            if (message.contains("wording")){
                return ResponseEntity.ok("\"Le libelle existe déjà !\"");
            }
            else {
                return ResponseEntity.ok("\"Une erreur est survenue !\"");
            }
        }

    }
    	/*----------------------------------------/
		/*        liste les categories           /
		/*-------------------------------------*/

    @RolesAllowed({ADMIN,MANAGER})
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }
    	/*----------------------------------------/
		/*        Affiche une categorie          /
		/*-------------------------------------*/

    @RolesAllowed({ADMIN,MANAGER})
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id){
        try {
            return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body("Une erreur est survenue !");
        }
    }
    	/*----------------------------------------/
		/*        modifie une categorie          /
		/*-------------------------------------*/

    @RolesAllowed({ADMIN,MANAGER})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id, @RequestBody Category category){
        try {
            return new ResponseEntity<>(categoryService.updateCategory(id, category), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body("Une erreur est survenue !");
        }
    }
    	/*----------------------------------------/
		/*        Supprime une cateegorie        /
		/*-------------------------------------*/

    @RolesAllowed({ADMIN,MANAGER})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id){
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body("Une erreur est survenue !");
        }
    }
}
