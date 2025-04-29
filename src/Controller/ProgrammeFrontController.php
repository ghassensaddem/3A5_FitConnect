<?php

namespace App\Controller;

use App\Repository\ProgrammeRepository;
use App\Repository\SeanceRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\Encoding\Encoding;
use Endroid\QrCode\ErrorCorrectionLevel\ErrorCorrectionLevel;

class ProgrammeFrontController extends AbstractController
{
    #[Route('/programme/front', name: 'app_programme_front')]
    public function index(Request $request, ProgrammeRepository $programmeRepository, SeanceRepository $seanceRepository): Response
    {
        $search = $request->query->get('search'); // récupérer la valeur du champ de recherche

        if ($search) {
            $programmes = $programmeRepository->createQueryBuilder('p')
                ->where('p.description LIKE :search')
                ->setParameter('search', '%' . $search . '%') // utiliser '%' pour la recherche partielle
                ->getQuery()
                ->getResult();
        } else {
            $programmes = $programmeRepository->findAll();
        }

        $seances = $seanceRepository->findAll();

        return $this->render('programme_front/index.html.twig', [
            'programmes' => $programmes,
            'seances' => $seances,
            'search' => $search,
        ]);
    }

    #[Route('/programme/{id}', name: 'app_programme_show', requirements: ['id' => '\d+'])]
    public function show(int $id, ProgrammeRepository $programmeRepository): Response
    {
        $programme = $programmeRepository->find($id);

        if (!$programme) {
            throw $this->createNotFoundException('Programme non trouvé.');
        }

        return $this->render('programme/show.html.twig', [
            'programme' => $programme,
        ]);
    }

    #[Route('/qrcode/seance/{id}', name: 'app_qrcode_seance')]
    public function generateQrCode(int $id): Response
    {
        // Génère l'URL vers la page de détails de la séance
        $url = $this->generateUrl('app_seance_details', ['id' => $id], 0);
    
        // Crée le QR code
        $qrCode = Builder::create()
            ->data($url)
            ->encoding(new Encoding('UTF-8'))
            ->size(200)
            ->margin(10)
            ->build();
    
        // Retourne une image PNG
        return new Response($qrCode->getString(), 200, [
            'Content-Type' => 'image/png',
        ]);
    }
}