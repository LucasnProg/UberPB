package org.example.model.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> implements Repository<T> {

    private final String filePath;
    private final Class<T> type;

    public JsonRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
    }

    public void salvar(List<T> entidades) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(entidades, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados em " + filePath, e);
        }
    }

    @Override
    public List<T> carregar() {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
