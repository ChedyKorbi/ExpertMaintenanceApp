<?php
include '../config/config.php';

// Récupération des données JSON depuis le corps de la requête
$data = json_decode(file_get_contents("php://input"), true);
$date = $data['date'] ?? null;
$site_id = $data['site_id'] ?? null;

// Préparation de la requête SQL
$queryStr = "SELECT * FROM interventions WHERE 1=1";
$params = [];

// Vérification et ajout de la condition de date
if ($date) {
    $queryStr .= " AND datedebut = :date";
    $params['date'] = $date;
}

// Vérification et ajout de la condition de site_id
if ($site_id) {
    $queryStr .= " AND site_id = :site_id";
    $params['site_id'] = $site_id;
}

try {
    // Préparation et exécution de la requête
    $query = $conn->prepare($queryStr);
    $query->execute($params);

    // Récupération des résultats
    $interventions = $query->fetchAll(PDO::FETCH_ASSOC);

    // Retour des résultats en JSON
    echo json_encode($interventions);
} catch (Exception $e) {
    // Gestion des erreurs et affichage d'un message approprié
    echo json_encode(["error" => "Database error: " . $e->getMessage()]);
}
?>
