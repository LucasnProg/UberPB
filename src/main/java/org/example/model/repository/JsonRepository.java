package org.example.model.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.util.LocalDateTimeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository<T> implements Repository<T> {

    private final String filePath;
    private final Class<T> type;
    private final Gson gson;

    public JsonRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void salvar(List<T> entidades) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(entidades, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados em " + filePath, e);
        }
    }

    @Override
    public List<T> carregar() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            List<T> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar dados de " + filePath, e);
        }
    }

}
