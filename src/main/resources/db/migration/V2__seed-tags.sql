insert into tags(type) values
                           ('VITORIA'), ('DESABAFO'), ('GATILHOS'), ('MOTIVACAO'), ('DUVIDA')
on conflict (type) do nothing;