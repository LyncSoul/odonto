package com.odonto.resource;

import com.odonto.model.Usuario;
import com.odonto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioRepository acessoRepository;

    @GetMapping("")
    public List<Usuario> getAcessos() {
        return acessoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAcessoById(@PathVariable Long id) {
        Optional<?> acessoProcurado = acessoRepository.findById(id);
        return acessoProcurado.isPresent() ? ResponseEntity.ok(acessoProcurado) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> acessoCadastro(@RequestBody @Valid Usuario acesso) {
        return ResponseEntity.ok().body(acessoRepository.save(acesso));
    }
}
