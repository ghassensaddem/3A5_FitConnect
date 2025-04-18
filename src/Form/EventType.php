<?php

namespace App\Form;

use App\Entity\Event;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Form\CallbackTransformer;

class EventType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('date', TextType::class, [
                'label' => 'Date (format YYYY-MM-DD)',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'La date est obligatoire.',
                    ]),
                    new Assert\Regex([
                        'pattern' => '/^\\d{4}-\\d{2}-\\d{2}$/',
                        'message' => 'Le format de la date doit être YYYY-MM-DD.',
                    ]),
                    new Assert\Callback(function ($date, $context) {
                        if ($date) {
                            $today = new \DateTime();
                            $today->setTime(0, 0, 0); // Normaliser pour éviter les erreurs de comparaison
                            $inputDate = \DateTime::createFromFormat('Y-m-d', $date);
                            if (!$inputDate || $inputDate < $today) {
                                $context->buildViolation('La date doit être supérieure à aujourd\'hui.')
                                    ->addViolation();
                            }
                        }
                    })
                ],
                'attr' => ['class' => 'form-control'],
            ])
            ->add('prixdupass', NumberType::class, [
                'label' => 'Prix du Pass',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Le prix est obligatoire.',
                    ]),
                    new Assert\PositiveOrZero([
                        'message' => 'Le prix doit être un nombre positif ou nul.',
                    ]),
                    new Assert\GreaterThanOrEqual([
                        'value' => 5.0,
                        'message' => 'Le prix doit être au minimum de 5.0.',
                    ]),
                    new Assert\LessThanOrEqual([
                        'value' => 20.0,
                        'message' => 'Le prix doit être au maximum de 20.0.',
                    ])
                ],
                'attr' => ['class' => 'form-control'],
            ])
            ->add('lieu', TextType::class, [
                'label' => 'Lieu',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Le lieu est obligatoire.',
                    ])
                ],
                'attr' => ['class' => 'form-control'],
            ])
            ->add('horaire', TextType::class, [
                'label' => 'Horaire',
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'L horaire est obligatoire.',
                    ])
                ],
                'attr' => ['class' => 'form-control'],
            ])
            ->add('image', FileType::class, [
                'label' => 'Photo de profil',
                'mapped' => false,
                'required' => true,
                'constraints' => [
                    new Assert\NotBlank([
                        'message' => 'Limage est obligatoire.',
                    ])
                ], 
                'attr' => ['class' => 'form-control'],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Event::class,
        ]);
    }
}
