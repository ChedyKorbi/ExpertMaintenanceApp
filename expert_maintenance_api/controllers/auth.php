<?php
include '../config/config.php';

header("Content-Type: application/json"); // Définit le type de contenu comme JSON

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, true);

$login = $input['login'] ?? null;
$pwd = $input['password'] ?? null;

if (empty($login) || empty($pwd)) {
    echo json_encode(["success" => false, "message" => "Identifiants manquants"]);
    exit;
}

$query = $conn->prepare("SELECT id, nom AS name, email FROM employes WHERE login = :login AND pwd = :pwd AND actif = 1");
$query->execute(['login' => $login, 'pwd' => $pwd]);
$user = $query->fetch(PDO::FETCH_ASSOC);

if ($user) {
    echo json_encode(["success" => true, "message" => "Identifiants valides", "data" => $user]);
} else {
    echo json_encode(["success" => false, "message" => "Identifiants incorrects ou compte inactif"]);
}
?>
