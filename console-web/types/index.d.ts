declare namespace Base {
  type FormMode = 'create' | 'edit';

  type FormModelProps<T, R> = {
    open: boolean;
    mode: FormMode;
    initialValues?: Partial<T>;
    onSubmit: (values: R) => Promise<any>;
    onCancel: () => void;
  }

  type ProblemDetail = {
    type?: string;
    title?: string;
    status?: number;
    detail?: string;
    instance?: string;

    [key: string]: any;
  };
}