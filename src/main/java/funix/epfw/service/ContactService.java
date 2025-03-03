package funix.epfw.service;

import funix.epfw.model.Contact;
import funix.epfw.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    // Add contact
    public void addContact(Contact contact) {

        contactRepository.save(contact);

    }

    //Get all contacts
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }
}
