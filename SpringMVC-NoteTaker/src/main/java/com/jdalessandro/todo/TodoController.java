package com.jdalessandro.todo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TodoController {

    private Log logger = LogFactory.getLog(TodoController.class);

    @Autowired
    TodoService service;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @RequestMapping(value = "/list-todos", method= RequestMethod.GET)
    public String listTodos(ModelMap model) {
        String user = retrieveLoggedInUserName();
        model.addAttribute("todos", service.retrieveTodos(retrieveLoggedInUserName()));
        logger.info("Retrieving List of Todos");
        return "list-todos";
    }

    private String retrieveLoggedInUserName() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();

        return principal.toString();
    }

    @RequestMapping(value = "/add-todo", method= RequestMethod.GET)
    public String showTodoPage(ModelMap model) {
        model.addAttribute("todo", new Todo(0, retrieveLoggedInUserName(), "", new Date(), false));
        return "todo";
    }

    @RequestMapping(value = "/add-todo", method= RequestMethod.POST)
    public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if(result.hasErrors()){
            return "todo";
        }
        service.addTodo(retrieveLoggedInUserName(), todo.getDesc(), new Date(), false);
        model.clear();
        logger.info("Added todo to list");
        return "redirect:list-todos";
    }

    @RequestMapping(value = "/update-todo", method= RequestMethod.GET)
    public String updateTodo(ModelMap model, @RequestParam int id) {
        Todo todo = service.retrieveTodo(id);
        model.addAttribute("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "/update-todo", method= RequestMethod.POST)
    public String updateTodo(ModelMap map, @Valid Todo todo, BindingResult result) {
        if(result.hasErrors()){
            return "todo";
        }
        todo.setUser(retrieveLoggedInUserName());
        service.updateTodo(todo);
        logger.info("Updated todo");
        return "redirect:list-todos";
    }

    @RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(ModelMap model, @RequestParam int id) {
        service.deleteTodo(id);
        model.clear();
        logger.info("Deleted todo");
        return "redirect:list-todos";
    }

    @RequestMapping(value = "/completed-todo", method = RequestMethod.GET)
    public String completeTodo(ModelMap model, @RequestParam int id) {
        service.completeTodo(id);
        model.clear();
        logger.info("Changed todo status to complete");
        return "redirect:list-todos";
    }

}
