<?php
include '../config/config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $intervention_id = $_POST['intervention_id'];
    $dateCapture = date('Y-m-d');

    // Vérifier si un fichier image a été envoyé
    if (isset($_FILES['image']) && $_FILES['image']['error'] == 0) {
        $nomImage = $_FILES['image']['name'];
        $cheminImage = '../uploads/' . basename($nomImage); // Chemin de destination

        // Déplacer le fichier image dans le dossier "uploads"
        if (move_uploaded_file($_FILES['image']['tmp_name'], $cheminImage)) {
            // Enregistrer le nom et le chemin dans la base de données, sans stocker le fichier lui-même
            $query = $conn->prepare("INSERT INTO images (nom, dateCapture, intervention_id, valsync) VALUES (:nom, :dateCapture, :intervention_id, 0)");
            $query->execute([
                'nom' => $nomImage,
                'dateCapture' => $dateCapture,
                'intervention_id' => $intervention_id
            ]);

            echo json_encode(["success" => true, "message" => "Image enregistrée avec succès"]);
        } else {
            echo json_encode(["success" => false, "message" => "Erreur lors de l'enregistrement de l'image"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Aucune image n'a été fournie ou erreur de transfert"]);
    }
}
?>

