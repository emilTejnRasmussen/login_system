package domain;

public record UserAuthRow(int id, String username, String email, String passwordHash)
{
}
