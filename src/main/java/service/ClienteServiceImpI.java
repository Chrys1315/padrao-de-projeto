/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.Optional;
import model.Cliente;
import model.ClienteRepository;
import model.Endereco;
import model.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lucas Chrysostomo
 */
public class ClienteServiceImpI implements ClienteService {
    
    // TOdo = fazer;
    // TODO Singleton: Injetar os componentes do Spring com @Autowired.
  
    @Autowired
    private ClienteRepository clienteRepository; 
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private ViaCepService viaCepService;
    
    //TODO Strategy: Implementar os metodos definidos na interface.
    //TODO Facade: Abstrair integrações com subsistemas provendo uma interface simples;
    

            
        
    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
   
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
        
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
         
    }
    
   

    @Override
    public void atualizar(Long id, Cliente cliente) {
        
       
        //FIXME Busar Cliente por ID, caso exista: 
       Optional<Cliente> clienteBd = clienteRepository.findById(id);
       
       if(clienteBd.isPresent()) {
           salvarClienteComCep(cliente);
           
       }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
        
    }
    
 
    private void salvarClienteComCep(Cliente cliente) {
         //FIXME Verificar se o Endereco do CLiente ja existe (pelo CEP).
       String cep = cliente.getEndereco().getCep();
       Endereco endereco = enderecoRepository.findById(cep).orElseGet(() ->{
           //Caso não exista, integrar com o ViaCEP e persistir o retorno.
           Endereco novoEndereco = viaCepService.consultarCep(cep);
           enderecoRepository.save(novoEndereco);
           return novoEndereco;
       });
       cliente.setEndereco(endereco);
       //Inserir Cliente, vinculando o Endereco (novo ou existente)
       clienteRepository.save(cliente);
   
    }


    private static class clienteRepository {

        public clienteRepository() {
        }
    }
    
}
