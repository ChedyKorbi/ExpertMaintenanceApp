<?php
include '../config/config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Récupérer les données JSON brutes
    $data = json_decode(file_get_contents("php://input"), true);

    // Vérification des données
    $id = isset($data['id']) ? $data['id'] : null;
    $titre = isset($data['titre']) ? $data['titre'] : null;
    $datedebut = isset($data['datedebut']) ? $data['datedebut'] : null;
    $datefin = isset($data['datefin']) ? $data['datefin'] : null;
    $commentaires = isset($data['commentaires']) ? $data['commentaires'] : null;

    // Validation basique des champs requis
    if ($titre === null || $datedebut === null || $datefin === null) {
        echo json_encode(["success" => false, "message" => "Titre, date de début et date de fin sont obligatoires."]);
        exit;
    }

    if ($id) {
        // Mise à jour
        $query = $conn->prepare("UPDATE interventions SET titre = :titre, datedebut = :datedebut, datefin = :datefin, commentaires = :commentaires WHERE id = :id");
        $query->execute([
            'id' => $id,
            'titre' => $titre,
            'datedebut' => $datedebut,
            'datefin' => $datefin,
            'commentaires' => $commentaires
        ]);
    } else {
        // Insertion
        $query = $conn->prepare("INSERT INTO interventions (titre, datedebut, datefin, commentaires) VALUES (:titre, :datedebut, :datefin, :commentaires)");
        $query->execute([
            'titre' => $titre,
            'datedebut' => $datedebut,
            'datefin' => $datefin,
            'commentaires' => $commentaires
        ]);
    }

    echo json_encode(["success" => true]);
}
?>
