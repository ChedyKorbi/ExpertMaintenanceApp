<?php
include '../config/config.php';

if (isset($_GET['id'])) {
    $interventionId = intval($_GET['id']);

    // Préparer la requête SQL pour récupérer les informations du client
    $query = $conn->prepare("
        SELECT 
            clients.id AS client_id,
            clients.nom AS client_nom,
            clients.adresse AS client_adresse,
            clients.tel AS client_tel,
            clients.email AS client_email,
            clients.contact AS client_contact,
            clients.telcontact AS client_telcontact
        FROM 
            interventions
        INNER JOIN 
            sites ON interventions.site_id = sites.id
        INNER JOIN 
            clients ON sites.client_id = clients.id
        WHERE 
            interventions.id = :interventionId
    ");

    $query->bindParam(':interventionId', $interventionId, PDO::PARAM_INT);

    try {
        $query->execute();

        if ($query->rowCount() > 0) {
            $client = $query->fetch(PDO::FETCH_ASSOC);

            echo json_encode([
                'data' => [
                    'nom' => $client['client_nom'],
                    'adresse' => $client['client_adresse'],
                    'contact' => $client['client_contact'],
                    'tel' => $client['client_tel'],
                    'email' => $client['client_email'],
                    'telcontact' => $client['client_telcontact'],
                ]
            ]);
        } else {
            echo json_encode([
                'success' => false,
                'message' => 'Aucun client trouvé pour cette intervention'
            ]);
        }
    } catch (Exception $e) {
        echo json_encode([
            'success' => false,
            'message' => 'Erreur lors de la récupération des données : ' . $e->getMessage()
        ]);
    }
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Paramètre ID manquant'
    ]);
}
?>
