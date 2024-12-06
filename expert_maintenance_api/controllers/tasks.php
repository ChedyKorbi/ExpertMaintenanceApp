<?php
include '../config/config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_POST['id'];
    $nom = $_POST['nom'];
    $duree = $_POST['duree'];
    $prixheure = $_POST['prixheure'];
    $intervention_id = $_POST['intervention_id'];

    if ($id) {
        // Mise à jour
        $query = $conn->prepare("UPDATE taches SET nom = :nom, duree = :duree, prixheure = :prixheure WHERE id = :id");
        $query->execute(['id' => $id, 'nom' => $nom, 'duree' => $duree, 'prixheure' => $prixheure]);
    } else {
        // Ajout
        $query = $conn->prepare("INSERT INTO taches (nom, duree, prixheure, intervention_id) VALUES (:nom, :duree, :prixheure, :intervention_id)");
        $query->execute(['nom' => $nom, 'duree' => $duree, 'prixheure' => $prixheure, 'intervention_id' => $intervention_id]);
    }

    echo json_encode(["success" => true]);
}
?>
