<?php
include '../config/config.php';

$tables = ['clients', 'contrats', 'employes', 'interventions', 'sites', 'taches', 'images'];
$data = [];

foreach ($tables as $table) {
    // Sélectionner uniquement les nouvelles données avec valsync = 1
    $query = $conn->query("SELECT * FROM $table WHERE valsync = 1");
    $data[$table] = $query->fetchAll(PDO::FETCH_ASSOC);

    // Mettre à jour la table pour indiquer que les données ont été synchronisées
    $conn->query("UPDATE $table SET valsync = 0 WHERE valsync = 1");
}

// Renvoie les nouvelles données en JSON
echo json_encode($data);
?>
