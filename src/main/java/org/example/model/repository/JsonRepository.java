package org.example.model.repository;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public void salvar(List<T> entidades) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); // cria diretórios se não existirem
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(entidades, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar dados em " + filePath, e);
        }
    }

    @Override
    public List<T> carregar() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            List<T> lista = gson.fromJson(reader, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar dados de " + filePath, e);
        }
    }

    // Adapter para serializar e desserializar LocalDateTime
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(FORMATTER));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString(), FORMATTER);
        }
    }
}
