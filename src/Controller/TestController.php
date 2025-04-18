<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class TestController extends AbstractController
{
    #[Route('/test', name: 'app_test')]
    public function index(): Response
    {
        return $this->render('test/index.html.twig', [
            'controller_name' => 'TestController',
        ]);
    }
    #[Route('/show_service/{nom}', name: 'show')]
    public function show_service($nom): Response
    {
        return $this->render('test/show_service.twig', [
            'name' => $nom,
        ]);
    }
    #[Route('/redirect_home', name: 'redirect_home')]
    public function goToIndex(): Response
    {
        
        return $this->redirectToRoute('app_test');
          
    }






}
