import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class WeatherApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Forecast");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(245, 250, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel heading = new JLabel("🌤️ Weather App", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(30, 60, 90));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(heading, gbc);

        // City input label
        JLabel cityLabel = new JLabel("City Name:");
        cityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cityLabel.setForeground(new Color(30, 60, 90));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        frame.add(cityLabel, gbc);

        // City text field
        JTextField cityField = new JTextField();
        cityField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        frame.add(cityField, gbc);

        // Button
        JButton getWeatherBtn = new JButton("Get Weather");
        getWeatherBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        getWeatherBtn.setBackground(new Color(60, 130, 230));
        getWeatherBtn.setForeground(Color.WHITE);
        getWeatherBtn.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(getWeatherBtn, gbc);

        // Result area
        JTextArea resultArea = new JTextArea(8, 30);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        gbc.gridy = 3;
        frame.add(scrollPane, gbc);

        // Action
        getWeatherBtn.addActionListener(e -> {
            String city = cityField.getText().trim();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a city name.", "Missing Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String apiKey = "494d03c081f043c08ec214837252304";
                String apiUrl = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + URLEncoder.encode(city, "UTF-8") + "&aqi=yes";

                HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject location = json.getJSONObject("location");
                JSONObject current = json.getJSONObject("current");

                String output = String.format(
                        "City: %s\nCountry: %s\nTemperature: %.1f°C\nWind: %.1f kph\nCondition: %s",
                        location.getString("name"),
                        location.getString("country"),
                        current.getDouble("temp_c"),
                        current.getDouble("wind_kph"),
                        current.getJSONObject("condition").getString("text")
                );

                resultArea.setText(output);

            } catch (Exception ex) {
                resultArea.setText("❌ Failed to fetch data. Please check the city name or try again.");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
