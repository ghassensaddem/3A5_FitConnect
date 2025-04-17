<?php

namespace App\Form;

use App\Entity\Client;
use App\Entity\Event;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;


class ProfileType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('nom', TextType::class, [
            'label' => 'Nom',
            'required' => false,
            'attr' => ['class' => 'infoProfile', 'placeholder' => 'Entrez votre nom'],
        ])
        ->add('prenom', TextType::class, [
            'label' => 'Prénom',
            'required' => false,
            'attr' => ['class' => 'infoProfile', 'placeholder' => 'Entrez votre prénom'],
        ])
        ->add('email', EmailType::class, [
            'label' => 'Email',
            'required' => false,
            'attr' => ['class' => 'infoProfile', 'placeholder' => 'Entrez votre email'],
        ])
        ->add('mdp', PasswordType::class, [
            'label' => 'Mot de passe',
            'required' => false,
            'mapped' => false,
            'attr' => ['class' => 'infoProfile', 'placeholder' => 'Choisissez un nouveau mdp'],
        ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Client::class,
            'csrf_protection' => true, // Assurez-vous que cette ligne est présente
            'csrf_field_name' => '_token', // Nom du champ caché pour CSRF
            'csrf_token_id'   => 'profile_update', // Identifiant du token CSRF
            'attr' => ['novalidate' => 'novalidate'],
        ]);
    }
}
