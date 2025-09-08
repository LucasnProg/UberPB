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

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // <-- REGISTRE O ADAPTADOR AQUI
            .setPrettyPrinting()
            .create();

    public JsonRepository(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
    }

    public void salvar(List<T> entidades) {
        try {
            File file = new File(filePath);

            // Garante que os diretórios existam
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(entidades, writer);
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados em " + filePath, e);
        }
    }

    @Override
    public List<T> carregar() {
        try (FileReader reader = new FileReader(filePath)) {
            // Use o mesmo objeto gson para carregar também
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            List<T> result = gson.fromJson(reader, listType);
            // Garante que não retorne nulo se o arquivo estiver vazio
            return result == null ? new ArrayList<>() : result;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
