package pl.sdacademy.docmanagement.simple;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.UUID;

/**
 * Przykład użycia repozytorium.
 */
public class DocumentRepositoryExample {
    private static MongoClient mongoClient;

    public static void main(String[] args) {
        try {
            createExampleDocument(createRepository());
        }
        finally {
            mongoClient.close();
        }
    }

    private static void createExampleDocument(DocumentRepository documentRepository) {
        // prepare an example document
        // document enters the system
        String documentId = UUID.randomUUID().toString();
        Document document = new Document(documentId);

        // document is associated with financial department and executive department
        Department financialDep = new Department("Financial Department");
        document.associateWith(financialDep);
        Department executiveDepartment = new Department("Executive Department");
        document.associateWith(executiveDepartment);
        // financial department finishes processing the document
        document.finishProcessingBy("Financial Department");
        // finish processing document by executive department
        document.finishProcessingBy("Executive Department");

        documentRepository.save(document);
        documentRepository.findById(documentId).ifPresent(System.out::print);
    }

    private static DocumentRepository createRepository() {
        //		Path dbDir = Paths.get("files", "docmanagement", "db");
        //		DocumentRepository documentRepository = new JsonDocumentRepository(dbDir);
        //		return documentRepository;
        mongoClient = MongoClients.create(codecSettings());
        return new MongoDbDocumentRepository(mongoClient);
    }

    private static MongoClientSettings codecSettings() {
        return MongoClientSettings.builder().codecRegistry(CodecRegistries.fromRegistries(com.mongodb
                .MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromProviders(PojoCodecProvider
                .builder().automatic(true).build()))).build();
    }
}
