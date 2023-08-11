package org.pedro.services;

import jakarta.persistence.EntityNotFoundException;
import org.pedro.dto.ClientDTO;
import org.pedro.entities.Client;
import org.pedro.repositories.ClientRepository;
import org.pedro.services.exceptions.DatabaseException;
import org.pedro.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
        Page<Client> list = repository.findAll(pageRequest);
        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Optional<Client> optionalClient = repository.findById(id);
        if (optionalClient.isEmpty()){
            throw new ResourceNotFoundException("Client with id: " + id + " not found");
        }
        Client entity = optionalClient.get();
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
    try {
        Client entity = repository.getReferenceById(id);
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
        } catch (EntityNotFoundException e){
            throw new NoSuchElementException("Client with id: " + id + " not found");
        }
    }

    public void delete(Long id){
        if(!repository.existsById(id)) {
            throw new DatabaseException("Id not found " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getName());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
