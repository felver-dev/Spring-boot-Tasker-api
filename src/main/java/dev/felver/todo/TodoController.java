package dev.felver.todo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoRepository repository;

    public TodoController(TodoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Todo> list() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable Long id) {
        return find(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@Valid @RequestBody TodoRequest request) {

        Todo todo = new Todo(request.title().trim());

        if (request.done() != null) {
            todo.setDone(request.done());
        }

        return repository.save(todo);
    }

    @PutMapping("{id}")
    public Todo update(@PathVariable Long id, @Valid @RequestBody TodoRequest request) {
        Todo todo = find(id);
        todo.setTitle(request.title().trim());

        if (request.done() != null) {
            todo.setDone(request.done());
        }
        return repository.save(todo);
    }

    @PatchMapping("{id}/toggle")
    public Todo toggle(@PathVariable Long id) {
        Todo todo = find(id);
        todo.setDone(!todo.isDone());
        return repository.save(todo);
    }

    @DeleteMapping ("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Todo todo = find(id);
        repository.delete(todo);
    }

    private Todo find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }

}
