interface pnxserviceOutput {
    isbn: string[] | null,
    source: string[] | null,
    record_id: string[] | null,
    publicationPlace: string[] | null,
    b_title: string[] | null,
    volume: string[] | null,
    creation_year: string[] | null,
    creator: string[] | null,
    pages: string[] | null,
    publisher: string[] | null,
    display_title: string[] | null,
    libraries: Library[] | null
}

interface Library {
    institution_code: string,
    display_name: string;
    mms_id: string,
    library_code: string
}