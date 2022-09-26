package com.odonto.resource;

import com.odonto.model.*;
import com.odonto.repository.DentistaRepository;
import com.odonto.repository.UsuarioRepository;
import com.odonto.service.GenericUserDetailsService;
import com.odonto.uploads.FileUpload;
import com.odonto.uploads.FileUploadUrl;
import com.odonto.uploads.FireBaseStorageService;
import com.odonto.util.JwtUtil;
import com.odonto.validations.ResponsesBody;
import com.odonto.validations.ValidacoesFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/dentistas")
public class DentistaResource {

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = true)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository acessoRepository;

    @Autowired
    private FireBaseStorageService fireBase;

    @Autowired
    private GenericUserDetailsService genericUserDetailService;

    @PostMapping("/autenticacao")
    public ResponseEntity<?> escolaLogin(@RequestBody AutenticationRequest acessoRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(acessoRequest.getLogin(), acessoRequest.getSenha()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsesBody.BAD_LOGIN);
        }

        Usuario acesso = acessoRepository.findByLogin(acessoRequest.getLogin()).get();
        final UserDetails userDetails = genericUserDetailService.loadUserByUsername(acesso.getLogin());
        Dentista dentista = dentistaRepository.findByUsuario(acesso);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok().body(new AuthenticationResponseProf(dentista, jwt));

    }
    @GetMapping("")
    public List<Dentista> getProfessors(){return dentistaRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getProfessor(@PathVariable Long id){
        return ResponseEntity.ok().body(dentistaRepository.findById(id));
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> escolaCadastro(@RequestBody @Valid Dentista dentista, BindingResult bindingResult) {

        // return ResponseEntity.ok().body(escolaRepository.save(escola));
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(ValidacoesFormat.formatarErros(bindingResult));

        Usuario acesso = dentista.getUsuario();
        if (acessoRepository.existsByLogin(acesso.getLogin()))
            return ResponseEntity.badRequest().body(ResponsesBody.LOGIN_CADASTRADO);
        else {
            Usuario login_senha = dentista.getUsuario();
            String senhaEncrypt = passwordEncoder.encode(dentista.getUsuario().getSenha());
            login_senha.setSenha(senhaEncrypt);
            dentista.setUsuario(login_senha);

            UserDetails userDetails = new GenericUserDetails(acesso);

            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok().body(new AuthenticationResponseProf(dentistaRepository.save(dentista), jwt));
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id){
        dentistaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void atualizar(@RequestBody Dentista dentista ){
        dentistaRepository.save(dentista);
    }

    @PostMapping("/upload/{codigo}")
    public ResponseEntity<FileUploadUrl> gravarFoto(@RequestBody FileUpload file, @PathVariable Long codigo){
        Optional<Dentista> dentistaProcurado = dentistaRepository.findById(codigo);

        if(dentistaProcurado.isPresent()){
            Dentista dentistaEncontrado = dentistaProcurado.get();

            FileUploadUrl url = new FileUploadUrl(fireBase.upLoad(file, dentistaEncontrado.getUsuario().getLogin()));
            dentistaEncontrado.setUrlFotoPerfil(url.getUrl());
            dentistaRepository.save(dentistaEncontrado);

            return ResponseEntity.ok(url);
        }
        return  ResponseEntity.notFound().build();
    }
}
