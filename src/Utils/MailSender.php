<?php

namespace App\Utils;

use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;

class MailSender
{
    private MailerInterface $mailer;

    public function __construct(MailerInterface $mailer)
    {
        $this->mailer = $mailer;
    }

    public function envoyerEmail(string $destinataire, string $sujet, string $contenu): bool
    {
        try {
            $email = (new Email())
                ->from('rayanferjani55@gmail.com')
                ->to($destinataire)
                ->subject($sujet)
                ->text($contenu);

            $this->mailer->send($email);

            return true;
        } catch (\Exception $e) {
            return false;
        }
    }
}
