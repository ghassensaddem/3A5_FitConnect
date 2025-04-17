<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

class ServiceController extends AbstractController
{
    #[Route('/showService/{name}/{id}', name: 'app_service')]
    public function showService($name , $id): Response
    {
        return $this->render('service/showService.html.twig', ["var" => $name ,"id" => $id
        ]);
    }


    #[Route('/gotoIndex', name: 'app_gotoIndex')]

    public function app_gotoIndex(): Response
    {
        return $this->render('home/index.html.twig', [
            'controller_name' => 'HomeController',
        ]);
    }
}
