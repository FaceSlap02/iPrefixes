package me.activated.prefixes.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.activated.prefixes.Prefixes;
import me.activated.prefixes.impl.Prefix;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class PlayerData {

    private final Prefixes plugin;
    private final String name;
    private final UUID uniqueId;

    private Prefix prefix;

    public void load() {
        try {
            PreparedStatement statement = this.plugin.getConnection().prepareStatement("SELECT * FROM playerdata where uuid=?");
            statement.setString(1, this.uniqueId.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                this.prefix = Prefix.valueOf(result.getString("prefix"));
            }
            this.plugin.close(statement, result);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void save() {
        try {
            PreparedStatement statement = this.plugin.getConnection().prepareStatement("SELECT * FROM playerdata WHERE uuid=?");
            statement.setString(1, this.uniqueId.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                PreparedStatement update = this.plugin.getConnection().prepareStatement("UPDATE " +
                        "playerdata SET name=?,uuid=?,nameLowerCase=?,prefix=? WHERE uuid=?");

                update.setString(1, this.name);
                update.setString(2, this.uniqueId.toString());
                update.setString(3, this.name.toLowerCase());
                update.setString(4, this.prefix.toString());
                update.setString(5, this.uniqueId.toString());

                update.executeUpdate();
                update.close();
            } else {
                PreparedStatement insert = this.plugin.getConnection().prepareStatement("INSERT INTO " +
                        "playerdata (name,uuid,nameLowerCase,prefix) VALUES (?,?,?,?)");

                insert.setString(1, this.name);
                insert.setString(2, this.uniqueId.toString());
                insert.setString(3, this.name.toLowerCase());
                insert.setString(4, this.prefix.toString());

                insert.executeUpdate();
                insert.close();
            }

            this.plugin.close(statement, result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
