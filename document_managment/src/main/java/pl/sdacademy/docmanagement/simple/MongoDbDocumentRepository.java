package pl.sdacademy.docmanagement.simple;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Optional;

public class MongoDbDocumentRepository implements DocumentRepository {

    MongoCollection<Document> documentsCollection;

    public MongoDbDocumentRepository(MongoClient mongoClient) {
        MongoDatabase documentDB = mongoClient.getDatabase("documentDB");
        this.documentsCollection = documentDB.getCollection("document", Document.class);

    }

    @Override
    public void save(Document document) throws DocumentRepositoryException {
        documentsCollection.insertOne(document);
    }

    @Override
    public Optional<Document> findById(String id) throws DocumentRepositoryException {

        return Optional.ofNullable(documentsCollection.find(Filters.eq("_id",id)).first());

    }


}
