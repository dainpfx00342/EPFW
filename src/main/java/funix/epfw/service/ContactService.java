package funix.epfw.service;

import funix.epfw.model.Contact;
import funix.epfw.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void addContact(Contact contact) {
        // Add contact
        contactRepository.save(contact);
    }
}
