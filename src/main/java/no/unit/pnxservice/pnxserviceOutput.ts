interface pnxserviceOutput {
    isbn?: string[],
    source?: string[],
    record_id?: string[],
    publicationPlace?: string[],
    b_title?: string[],
    volume?: string[],
    creation_year?: string[],
    creator?: string[],
    pages?: string[],
    publisher?: string[],
    display_title?: string[],
    libraries: Library[]
}

interface Library {
    institution_code: string,
    display_name: string;
    mms_id: string,
    library_code: string
}