<?php
namespace App\Form;

use App\Entity\Author;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Extension\Core\Type\TextType; // Pour le champ texte
use Symfony\Component\Form\Extension\Core\Type\EmailType; // Pour le champ email
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AuthorType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            // Champ pour le nom d'utilisateur (nom)
            ->add('username', TextType::class, [
                'label' => 'Nom',
                'attr' => ['class' => 'form-control'],
            ])
            
            // Champ pour l'email
            ->add('email', EmailType::class, [
                'label' => 'Email',
                'attr' => ['class' => 'form-control'],
            ])

            // Bouton pour enregistrer ou modifier
            ->add('save', SubmitType::class, [
                'label' => $options['is_edit'] ? 'Modifier' : 'Enregistrer',
                'attr' => ['class' => 'btn btn-primary'],
            ])
            ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Author::class,
            'is_edit' => false, // Indicateur pour savoir si c'est un mode d'édition
        ]);
    }
}
